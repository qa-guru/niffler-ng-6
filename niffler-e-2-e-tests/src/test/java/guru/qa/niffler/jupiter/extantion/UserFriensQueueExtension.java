package guru.qa.niffler.jupiter.extantion;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class UserFriensQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserFriensQueueExtension.class);

    public  record StaticUser (String username, String password, String friends, String income, String outcome){}

    private  static  final Queue<StaticUser> EMPTY_USER = new ConcurrentLinkedDeque<>();
    private  static  final Queue<StaticUser> WITH_FRIEND_USER = new ConcurrentLinkedDeque<>();
    private  static  final Queue<StaticUser> WITH_INCOME_REQUEST_USER = new ConcurrentLinkedDeque<>();
    private  static  final Queue<StaticUser>  WITH_OUTCOME_REQUEST_USER = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USER.add(new StaticUser("user1", "12345", null, null, null));
        WITH_FRIEND_USER.add(new StaticUser("esa", "12345", "user1", null, null));
        WITH_INCOME_REQUEST_USER.add(new StaticUser("user2", "12345", null, "user4", null));
        WITH_OUTCOME_REQUEST_USER.add(new StaticUser("user3", "12345", null, null, "user5"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;
        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .findFirst()
                .map(p -> p.getAnnotation(UserType.class))
                .ifPresent(
                        ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                if (ut.value() == UserType.Type.EMPTY) {
                                    Optional.ofNullable(EMPTY_USER.poll());
                                } else if (ut.value() == UserType.Type.WITH_FRIEND) {
                                    Optional.ofNullable(WITH_FRIEND_USER.poll());
                                } else if (ut.value() == UserType.Type.WITH_INCOME_REQUEST) {
                                    Optional.ofNullable(WITH_INCOME_REQUEST_USER.poll());
                                } else if (ut.value() == UserType.Type.WITH_OUTCOME_REQUEST) {
                                    Optional.ofNullable(WITH_OUTCOME_REQUEST_USER.poll());
                                }
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> {
                                testCase.setStart(new Date().getTime());
                            });
                            user.ifPresentOrElse(
                                    u -> {
                                        context.getStore(NAMESPACE)
                                                .put(
                                                        context.getUniqueId(),
                                                        u
                                                );
                                    },
                                    () -> new IllegalStateException("Can't find user after 30 seconds")
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        StaticUser user = context.getStore(NAMESPACE).get(context.getUniqueId(), StaticUser.class);
        if (user.friends == null && user.income == null && user.outcome == null) {
            EMPTY_USER.add(user);
        } else if (user.friends != null && user.income == null && user.outcome == null) {
            WITH_FRIEND_USER.add(user);
        } else if (user.friends == null && user.income != null && user.outcome == null) {
            WITH_INCOME_REQUEST_USER.add(user);
        } else if (user.friends == null && user.income == null && user.outcome != null) {
            WITH_OUTCOME_REQUEST_USER.add(user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);

    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), StaticUser.class);
    }
}
