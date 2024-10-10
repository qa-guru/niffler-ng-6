package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.enums.UserType;
import guru.qa.niffler.jupiter.annotation.UserFromQueue;
import guru.qa.niffler.model.StaticUser;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.enums.UserType.*;

@Slf4j
public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);
    static final Queue<StaticUser> EMPTY_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_QUEUE = new ConcurrentLinkedQueue<>();

    static {

        EMPTY_QUEUE.add(StaticUser.builder().username("without_friend_1").password("12345").userType(UserType.EMPTY).build());
        EMPTY_QUEUE.add(StaticUser.builder().username("without_friend_2").password("12345").userType(UserType.EMPTY).build());
        EMPTY_QUEUE.add(StaticUser.builder().username("without_friend_3").password("12345").userType(UserType.EMPTY).build());

        WITH_INCOME_REQUEST_QUEUE.add(StaticUser.builder().username("got_friend_request_1").password("12345").userType(WITH_INCOME_REQUEST).incomeRequestFromUsersList(List.of("sent_friend_request_1", "sent_friend_request_2", "sent_friend_request_3")).build());
        WITH_INCOME_REQUEST_QUEUE.add(StaticUser.builder().username("got_friend_request_2").password("12345").userType(WITH_INCOME_REQUEST).incomeRequestFromUsersList(List.of("sent_friend_request_1", "sent_friend_request_2", "sent_friend_request_3")).build());
        WITH_INCOME_REQUEST_QUEUE.add(StaticUser.builder().username("got_friend_request_3").password("12345").userType(WITH_INCOME_REQUEST).incomeRequestFromUsersList(List.of("sent_friend_request_1", "sent_friend_request_2", "sent_friend_request_3")).build());

        WITH_OUTCOME_REQUEST_QUEUE.add(StaticUser.builder().username("sent_friend_request_1").password("12345").userType(WITH_OUTCOME_REQUEST).outcomeRequestToUsersList(List.of("got_friend_request_1", "got_friend_request_2", "got_friend_request_3")).build());
        WITH_OUTCOME_REQUEST_QUEUE.add(StaticUser.builder().username("sent_friend_request_2").password("12345").userType(WITH_OUTCOME_REQUEST).outcomeRequestToUsersList(List.of("got_friend_request_1", "got_friend_request_2", "got_friend_request_3")).build());
        WITH_OUTCOME_REQUEST_QUEUE.add(StaticUser.builder().username("sent_friend_request_3").password("12345").userType(WITH_OUTCOME_REQUEST).outcomeRequestToUsersList(List.of("got_friend_request_1", "got_friend_request_2", "got_friend_request_3")).build());

        WITH_FRIEND_QUEUE.add(StaticUser.builder().username("with_friend_1").password("12345").userType(WITH_OUTCOME_REQUEST).friendsList(List.of("with_friend_2", "with_friend_3")).build());
        WITH_FRIEND_QUEUE.add(StaticUser.builder().username("with_friend_2").password("12345").userType(WITH_OUTCOME_REQUEST).incomeRequestFromUsersList(List.of("with_friend_1", "with_friend_3")).build());
        WITH_FRIEND_QUEUE.add(StaticUser.builder().username("with_friend_3").password("12345").userType(WITH_OUTCOME_REQUEST).incomeRequestFromUsersList(List.of("with_friend_1", "with_friend_1")).build());

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {

        log.info("Queues size: WITHOUT_USERS_SIZE = [{}], GOT_FRIEND_REQUEST_QUEUE_SIZE = [{}], SENT_FRIEND_REQUEST_QUEUE_SIZE = [{}], WITH_FRIENDS_QUEUE_SIZE = [{}]", EMPTY_QUEUE.size(), WITH_INCOME_REQUEST_QUEUE.size(), WITH_OUTCOME_REQUEST_QUEUE.size(), WITH_FRIEND_QUEUE.size());

        // Get parameters of test method
        Arrays.stream(context.getRequiredTestMethod().getParameters())

                // Filter parameters which has annotation @UserType and get first
                .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserFromQueue.class))
                .forEach(parameter -> {
                    var parameterName = parameter.getName();
                    UserFromQueue anno = parameter.getAnnotation(UserFromQueue.class);
                    Queue<StaticUser> usersQueue = getUsersQueueByStaticUserType(anno.value());
                    Optional<StaticUser> user = Optional.empty();

                    StopWatch sw = StopWatch.createStarted();

                    // if user is empty and timeout < 30 seconds try to get user from queue
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(usersQueue.poll());
                    }

                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );

                    user.ifPresentOrElse(
                            u -> {
                                @SuppressWarnings("unchecked")
                                Map<String, StaticUser> usersMap = (Map<String, StaticUser>) context.getStore(NAMESPACE)
                                        .getOrComputeIfAbsent(context.getUniqueId(), map -> new HashMap<>());
                                usersMap.put(parameterName, u);
                            },
                            () -> {
                                throw new IllegalStateException("Can`t obtain user after 30s.");
                            }
                    );

                });
    }

    private static Queue<StaticUser> getUsersQueueByStaticUserType(UserType userType) {
        return switch (userType) {
            case EMPTY -> EMPTY_QUEUE;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_QUEUE;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_QUEUE;
            case WITH_FRIEND -> WITH_FRIEND_QUEUE;
        };
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {

        @SuppressWarnings("unchecked")
        Map<String, StaticUser> usersMap = (Map<String, StaticUser>) context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        if (usersMap != null)
            usersMap.values().forEach(user -> getUsersQueueByStaticUserType(user.getUserType()).add(user));

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserFromQueue.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        @SuppressWarnings("unchecked")
        Map<String, StaticUser> usersMap = (Map<String, StaticUser>) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);
        return usersMap.get(parameterContext.getParameter().getName());
    }

}
