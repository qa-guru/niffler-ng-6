package guru.qa.niffler.jupiter.extension;

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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String incoming,
            String outgoing) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOMING_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTGOING_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "dima", null, null));
        WITH_FRIEND_USERS.add(new StaticUser("dima", "12345", "duck", null, null));
        WITH_INCOMING_REQUEST_USERS.add(new StaticUser("bill", "12345", null, "comrade", null));
        WITH_OUTGOING_REQUEST_USERS.add(new StaticUser("comrade", "12345", null, null, "bill"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOMING_REQUEST, WITH_OUTGOING_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> {
                    UserType ut = p.getAnnotation(UserType.class);
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = switch (ut.value()) {
                            case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
                            case WITH_INCOMING_REQUEST -> Optional.ofNullable(WITH_INCOMING_REQUEST_USERS.poll());
                            case WITH_OUTGOING_REQUEST -> Optional.ofNullable(WITH_OUTGOING_REQUEST_USERS.poll());
                            default -> Optional.ofNullable(EMPTY_USERS.poll());
                        };
                    }
                    Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));

                    user.ifPresentOrElse(
                            u -> context.getStore(NAMESPACE).put(context.getUniqueId() + p.getName(), u),
                            () -> new IllegalStateException("Can't find a user after 30 sec")
                    );
                    return user;
                }).forEach(Optional::isPresent);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                    String key = context.getUniqueId() + p.getName();
                    StaticUser user = context.getStore(NAMESPACE).get(key, StaticUser.class);
                    if (user != null) {
                        UserType ut = p.getAnnotation(UserType.class);
                        switch (ut.value()) {
                            case WITH_FRIEND -> WITH_FRIEND_USERS.add(user);
                            case WITH_INCOMING_REQUEST -> WITH_INCOMING_REQUEST_USERS.add(user);
                            case WITH_OUTGOING_REQUEST -> WITH_OUTGOING_REQUEST_USERS.add(user);
                            default -> EMPTY_USERS.add(user);
                        }
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String key = extensionContext.getUniqueId() + parameterContext.getParameter().getName();
        return extensionContext.getStore(NAMESPACE).get(key, StaticUser.class);
    }
}
