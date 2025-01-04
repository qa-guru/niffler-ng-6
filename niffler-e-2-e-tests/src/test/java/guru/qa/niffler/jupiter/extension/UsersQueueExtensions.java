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
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtensions implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtensions.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> WITH_FRIENDS_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USERS.add(new StaticUser("cat", "12345", null, null, null));
        WITH_FRIENDS_USERS.add(new StaticUser("duck", "12345", "dog", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dog", "12345", null, "bill", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("wolf", "12345", null, null, "bill"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIENDS, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class) && p.getType().isAssignableFrom(StaticUser.class))
                .findFirst()
                .map(p -> p.getAnnotation(UserType.class))
                .ifPresent(
                        ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(getQueueByType(ut.value()).poll());
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> {
                                testCase.setStart(new Date().getTime());
                            });

                            user.ifPresentOrElse(
                                    u -> {
                                        ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                                .getOrComputeIfAbsent(context.getUniqueId(), key -> new HashMap<>())).put(ut, u);
                                    },
                                    () -> {
                                        throw new IllegalStateException("Can't find user after 30 seconds");
                                    }
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        if (map != null) {
            for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
                switch (e.getKey().value()) {
                    case EMPTY -> EMPTY_USERS.add(e.getValue());
                    case WITH_FRIENDS -> WITH_FRIENDS_USERS.add(e.getValue());
                    case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(e.getValue());
                    case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(e.getValue());
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map map = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        if (map == null) {
            throw new ParameterResolutionException("No user map found in the Store.");
        }
        UserType userTypeAnnotation = parameterContext.getParameter().getAnnotation(UserType.class);
        if (userTypeAnnotation == null) {
            throw new ParameterResolutionException("No @UserType annotation found on the parameter.");
        }

        return (StaticUser) map.get(userTypeAnnotation);
    }


    private static Queue<StaticUser> getQueueByType(UserType.Type type) {
        return switch (type) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIENDS -> WITH_FRIENDS_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }
}
