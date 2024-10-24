package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.kafka.common.protocol.types.Field;
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

    public record StaticUser(String username, String password, boolean empty) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> NOT_EMPTY_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345", true));
        EMPTY_USERS.add(new StaticUser("bee1", "12345", true));
        NOT_EMPTY_USERS.add(new StaticUser("duck", "12345", false));
        NOT_EMPTY_USERS.add(new StaticUser("dima", "12345", false));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        boolean empty() default true;
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
                        user = Optional.ofNullable(ut.empty()
                                ? EMPTY_USERS.poll()
                                : NOT_EMPTY_USERS.poll());
                    }
                    Allure.getLifecycle().updateTestCase(testCase -> {
                        testCase.setStart(new Date().getTime());
                    });
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
                        if (user.empty()) {
                            EMPTY_USERS.add(user);
                        } else {
                            NOT_EMPTY_USERS.add(user);
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
