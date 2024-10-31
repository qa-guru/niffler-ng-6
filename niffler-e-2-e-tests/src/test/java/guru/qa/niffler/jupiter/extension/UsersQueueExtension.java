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

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOMING_REQUEST, WITH_OUTGOING_REQUEST
        }
    }

    // Queues with test data
    private static final Map<UserType.Type, Queue<StaticUser>> USER_QUEUES = Map.of(
            UserType.Type.EMPTY, new ConcurrentLinkedQueue<>(List.of(
                    new StaticUser("bee", "12345", null, null, null)
            )),
            UserType.Type.WITH_FRIEND, new ConcurrentLinkedQueue<>(List.of(
                    new StaticUser("duck", "12345", "dima", null, null)
                    , new StaticUser("dima", "12345", "duck", null, null)
            )),
            UserType.Type.WITH_INCOMING_REQUEST, new ConcurrentLinkedQueue<>(List.of(
                    new StaticUser("bill", "12345", null, "comrade", null)
            )),
            UserType.Type.WITH_OUTGOING_REQUEST, new ConcurrentLinkedQueue<>(List.of(
                    new StaticUser("comrade", "12345", null, null, "bill")
            ))
    );

    private Optional<StaticUser> getUserFromQueue(UserType.Type type) {
        return Optional.ofNullable(USER_QUEUES.get(type).poll());
    }

    private void addUserToQueue(UserType.Type type, StaticUser user) {
        USER_QUEUES.get(type).add(user);
    }

    private String getUserMapKey(ExtensionContext context) {
        return context.getUniqueId() + "_userMap";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                    UserType ut = p.getAnnotation(UserType.class);
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = getUserFromQueue(ut.value());
                    }
                    Allure.getLifecycle().updateTestCase(
                            testCase -> testCase.setStart(new Date().getTime())
                    );
                    user.ifPresentOrElse(
                            u -> ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                    .getOrComputeIfAbsent(
                                            // Adding a suffix in case there are multiple maps available
                                            getUserMapKey(context),
                                            key -> new HashMap<>()
                                    )).put(ut, u),
                            () -> {
                                throw new IllegalStateException("Can't find a user after 30 sec");
                            }
                    );
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> userMap = context.getStore(NAMESPACE).get(
                getUserMapKey(context),
                Map.class
        );
        if (userMap != null) {
            for (Map.Entry<UserType, StaticUser> entry : userMap.entrySet()) {
                addUserToQueue(entry.getKey().value(), entry.getValue());
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
        return (StaticUser) extensionContext.getStore(NAMESPACE).get(getUserMapKey(extensionContext), Map.class)
                .get(
                        AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get()
                );
    }
}
