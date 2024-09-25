package guru.qa.niffler.jupiter.extensions;

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

public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser
            (String username,
             String password,
             String friend,
             String income,
             String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USERS.add(new StaticUser("duck", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("vladislav", "root", "leon", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("test2", "root", null, "test3", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("test3", "root", null, null, "test2"));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("test4", "root", null, null, "test3"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type type() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(
                        ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(getQueueByUserType(ut.type()).poll());
                            }
                            Allure.getLifecycle().updateTestCase(tc -> {
                                tc.setStart(new Date().getTime());
                            });
                            user.ifPresentOrElse(
                                    u -> {
                                        ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                                .getOrComputeIfAbsent(context.getUniqueId(),
                                                        k -> new HashMap<>())).put(ut, u);
                                    }, () -> {
                                        throw new IllegalStateException("Can't find user after 30 seconds");
                                    }
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (map != null) {
            for (Map.Entry<UserType, StaticUser> entry : map.entrySet()) {
                addUserInQueueByType(entry.getKey().type(), entry.getValue());
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
        Map<UserType, StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return map.get(parameterContext.getParameter().getAnnotation(UserType.class));
    }

    // хелпер для определения типа очереди пользователя по типу аннотации из контекста.
    // в результате получаем заранее подготовленного юзера из блка static
    private Queue<StaticUser> getQueueByUserType(UserType.Type ut) {
        switch (ut) {
            case UserType.Type.EMPTY -> {
                return EMPTY_USERS;
            }
            case UserType.Type.WITH_FRIEND -> {
                return WITH_FRIEND_USERS;
            }
            case UserType.Type.WITH_INCOME_REQUEST -> {
                return WITH_INCOME_REQUEST_USERS;
            }
            case UserType.Type.WITH_OUTCOME_REQUEST -> {
                return WITH_OUTCOME_REQUEST_USERS;
            }
            default -> throw new RuntimeException("Type of user don't matches with any Queue type");
        }
    }

    // хелпер для добавления пользователя в очередь, тип которой соответствует типу аннотации из контекста
    private void addUserInQueueByType(UserType.Type type, StaticUser user) {
        switch (type) {
            case EMPTY -> EMPTY_USERS.add(user);
            case WITH_FRIEND -> WITH_FRIEND_USERS.add(user);
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(user);
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(user);
            default -> throw new RuntimeException("Type of annotation don't matches with any queue type");
        }
    }
}
