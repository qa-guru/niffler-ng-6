package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);
    private static final String SEPARATOR = "-";

    public record StaticUser(String username, String password, boolean empty) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> NOT_EMPTY_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345", true));
        NOT_EMPTY_USERS.add(new StaticUser("duck", "12345", false));
        NOT_EMPTY_USERS.add(new StaticUser("dima", "12345", false));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default EMPTY;

        enum Type {
            EMPTY, NOT_EMPTY
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(parameter -> {
                    UserType userType = parameter.getAnnotation(UserType.class);
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    var queue = getQueueByType(userType.value());

                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(queue.poll());
                    }

                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );

                    user.ifPresentOrElse(
                            u -> getUsersContext(context).put(
                                    parameter.getName() + SEPARATOR + userType.value(),
                                    u
                            ),
                            () -> {
                                throw new IllegalStateException("Can't obtain user after 30s.");
                            }
                    );
                });
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<String, StaticUser> usersMap = getUsersContext(extensionContext);
        Optional<UserType> annotation = parameterContext.findAnnotation(UserType.class);
        StaticUser user = null;

        if (annotation.isPresent()) {
            user = usersMap.get(parameterContext.getParameter().getName() + SEPARATOR + annotation.get().value());
        }

        return user;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<String, StaticUser> users = getUsersContext(context);

        for (Map.Entry<String, StaticUser> userEntry : users.entrySet()) {
            StaticUser user = userEntry.getValue();
            var splitKey = userEntry.getKey().split(SEPARATOR);
            var getKey = splitKey[1];
            var queue = getQueueByType(UserType.Type.valueOf(getKey));
            queue.add(user);
        }
    }


    private Map<String, StaticUser> getUsersContext(ExtensionContext context) {
        return (Map<String, StaticUser>) context.getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        context.getUniqueId(),
                        key -> new HashMap<>()
                );
    }

    private Queue<StaticUser> getQueueByType(UserType.Type value) {
        return switch (value) {
            case EMPTY -> EMPTY_USERS;
            case NOT_EMPTY -> NOT_EMPTY_USERS;
        };
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }
}
