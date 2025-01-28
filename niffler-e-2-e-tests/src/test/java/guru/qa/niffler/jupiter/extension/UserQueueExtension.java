package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;


public class UserQueueExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser(String username, String password, String friends, String income, String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USER = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USER = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USER = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USER = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USER.add(new StaticUser("user1", "12345", null, null, null));
        WITH_FRIEND_USER.add(new StaticUser("esa", "12345", "duck", null, null));
        WITH_INCOME_REQUEST_USER.add(new StaticUser("user2", "12345", null, "test3", null));
        WITH_OUTCOME_REQUEST_USER.add(new StaticUser("test3", "12345", null, null, "user2"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    private Queue<StaticUser> returnQueueName(UserType.Type type) {
        Queue<StaticUser> staticUserQueue = null;
        switch (type) {
            case EMPTY -> staticUserQueue = EMPTY_USER;
            case WITH_FRIEND -> staticUserQueue = WITH_FRIEND_USER;
            case WITH_INCOME_REQUEST -> staticUserQueue = WITH_INCOME_REQUEST_USER;
            case WITH_OUTCOME_REQUEST -> staticUserQueue = WITH_OUTCOME_REQUEST_USER;
        }
        return staticUserQueue;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(
                        ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(returnQueueName(ut.value()).poll());
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> {
                                testCase.setStart(new Date().getTime());
                            });
                            user.ifPresentOrElse(
                                    u -> {
                                        ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                                .getOrComputeIfAbsent(
                                                        context.getUniqueId(),
                                                        key -> new HashMap<>()
                                                ))
                                                .put(ut, u);
                                    },
                                    () -> {
                                        throw new IllegalStateException("Can't find user after 30 seconds");
                                    }
                            );
                        }
                );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
            returnQueueName(e.getKey().value()).add(e.getValue());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        UserType userType = AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get();
        StaticUser user1 = map.get(userType);
        return user1;
    }
}