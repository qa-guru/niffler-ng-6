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
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
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
        EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "dima", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, "barsik", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("barsik", "12345", null, null, "dima"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY,
            WITH_FRIEND,
            WITH_INCOME_REQUEST,
            WITH_OUTCOME_REQUEST
        }
    }

    private Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        if (type == UserType.Type.EMPTY) {
            return EMPTY_USERS;
        } else if (type == UserType.Type.WITH_FRIEND) {
            return WITH_FRIEND_USERS;
        } else if (type == UserType.Type.WITH_INCOME_REQUEST) {
            return WITH_INCOME_REQUEST_USERS;
        } else if (type == UserType.Type.WITH_OUTCOME_REQUEST) {
            return WITH_OUTCOME_REQUEST_USERS;
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> usersMap = new HashMap<>();
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class) && p.getType().isAssignableFrom(StaticUser.class))
                .forEach(p -> {

                    // Получаем аннотацию @UserType для текущего параметра
                    UserType ut = p.getAnnotation(UserType.class);
                    Queue<StaticUser> queue = getQueueByUserType(ut.value());
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();  // Таймер для отслеживания времени

                    // Получаем пользователя из соответствующей очереди
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(queue.poll());
                    }
                    user.ifPresentOrElse(
                            u -> {
                                // Сохраняем найденного пользователя u в Map, где ключ — это UserType, полученный ранее.
                                usersMap.put(ut, u);
                            },
                            () -> {
                                throw new IllegalStateException("Can't obtain user after 30s.");
                            }
                    );
                });
        // Сохраняем локальную Map в store после завершения работы с ней
        context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);
        //  Обновляем тестовый кейс для Allure-отчёта, чтобы установить время начала теста на текущий момент.
        Allure.getLifecycle().updateTestCase(testCase ->
                testCase.setStart(new Date().getTime())
        );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        @SuppressWarnings("unchecked")
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        // Проходим по каждому элементу карты (Map)
        if (map != null) {
            for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
                UserType userType = e.getKey(); // Получаем ключ (UserType)
                StaticUser user = e.getValue(); // Получаем значение (StaticUser)
                // Возвращаем пользователей в соответствующие очереди по типу
                Queue<StaticUser> queue = getQueueByUserType(userType.value());
                queue.add(user);
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
        // Извлекаем карту пользователей или создаём новую, если её ещё нет
        @SuppressWarnings("unchecked")
        Map<UserType, StaticUser> userMap = (Map<UserType, StaticUser>) extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(extensionContext.getUniqueId(), key -> new HashMap<>());
        // Получаем аннотацию @UserType для текущего параметра
        UserType userTypeAnnotation = parameterContext.getParameter().getAnnotation(UserType.class);
        // Извлекаем пользователя из карты по ключу (аннотации @UserType)
        StaticUser staticUser = userMap.get(userTypeAnnotation);
        if (staticUser == null) {
            throw new ParameterResolutionException("Пользователь не найден для указанного типа UserType");
        }
        return staticUser;
    }
}