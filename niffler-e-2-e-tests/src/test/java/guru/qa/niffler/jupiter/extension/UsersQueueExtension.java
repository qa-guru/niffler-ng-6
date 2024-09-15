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
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username,
                             String password,
                             String friend,
                             String income,
                             String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("test", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("zveda", "12345", "duck", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("zveda", "12345", null, "dima", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, null, "zveda"));
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
    public void beforeTestExecution(ExtensionContext context) {
        //create the user map from the context store
        @SuppressWarnings("unchecked")
        Map<UserType.Type, StaticUser> userMap = (Map<UserType.Type, StaticUser>) context.getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        context.getUniqueId(),
                        key -> new HashMap<>()
                );

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                    UserType userType = p.getAnnotation(UserType.class);
                    StaticUser user = fetchUser(userType.value());

                    if (user == null) {
                        throw new IllegalStateException("Cannot obtain user of type " + userType.value() + " after 30 seconds.");
                    }
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );

                    // Store the user in the map with the appropriate UserType.Type key
                    userMap.put(userType.value(), user);
                });
        // Store the user map in the context store
        context.getStore(NAMESPACE).put(context.getUniqueId(), userMap);

    }

    private Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        return switch (type) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    private StaticUser fetchUser(UserType.Type userType) {
        Queue<StaticUser> queue = getQueueByUserType(userType);
        Optional<StaticUser> userOptional = Optional.empty();
        StopWatch sw = StopWatch.createStarted();
        while (userOptional.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
            userOptional = Optional.ofNullable(queue.poll());
        }
        return userOptional.orElse(null);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        @SuppressWarnings("unchecked")
        Map<UserType.Type, StaticUser> userMap = (Map<UserType.Type, StaticUser>) extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(extensionContext.getUniqueId(), key -> new HashMap<>());

        UserType userTypeAnnotation = parameterContext.findAnnotation(UserType.class).orElseThrow(
                () -> new ParameterResolutionException("Annotation @UserType not found")
        );

        StaticUser staticUser = userMap.get(userTypeAnnotation.value());
        if (staticUser == null) {
            throw new ParameterResolutionException("User not found for specified UserType");
        }
        return staticUser;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        // Retrieve the map of users that were used in the test
        @SuppressWarnings("unchecked")
        Map<UserType.Type, StaticUser> userMap = (Map<UserType.Type, StaticUser>) context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );

        // Return the users to the appropriate queues
        for (Map.Entry<UserType.Type, StaticUser> entry : userMap.entrySet()) {
            UserType.Type userType = entry.getKey();
            StaticUser user = entry.getValue();
            Queue<StaticUser> queue = getQueueByUserType(userType);
            queue.offer(user); // Use offer() to add the user back to the queue
        }
    }
}
