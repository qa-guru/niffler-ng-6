package guru.qa.niffler.jupiter.extantion;

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

public class UserQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser(String username, String password, boolean empty) {
    }

    private static final Queue<StaticUser> EMPTY_USER = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> NOT_EMPTY_USER = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USER.add(new StaticUser("user1", "12345", true));
        NOT_EMPTY_USER.add(new StaticUser("esa", "12345", false));
        NOT_EMPTY_USER.add(new StaticUser("user2", "12345", false));
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
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(
                        ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = ut.empty()
                                        ? Optional.ofNullable(EMPTY_USER.poll())
                                        : Optional.ofNullable(NOT_EMPTY_USER.poll());
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
                                        System.out.println(ut);
                                    },
                                    () -> new IllegalStateException("Can't find user after 30 seconds")
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
            System.out.println("return queue " + e.getValue());
            if (e.getKey().empty()) {
                EMPTY_USER.add(e.getValue());
            } else {
                NOT_EMPTY_USER.add(e.getValue());
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
        ArrayList<UserType> ut = new ArrayList<>();
        for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
            ut.add(e.getKey());
        }
        StaticUser user1 = map.get(ut.get(parameterContext.getIndex()));
        return user1;
    }
}