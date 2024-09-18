package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class UserQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser(String username, String password, boolean empty){

    }

    public static final Queue<StaticUser> EMPTY_USER = new ConcurrentLinkedDeque<>();
    public static final Queue<StaticUser> NOT_EMPTY_USER = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USER.add(new StaticUser("bee","1234",true));
        NOT_EMPTY_USER.add(new StaticUser("duck","12345",false));
        NOT_EMPTY_USER.add(new StaticUser("superduck","12345",false));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        boolean empty() default true;

    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters()).filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .findFirst()
                .map(p -> p.getAnnotation(UserType.class))
                .ifPresent(
                        ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30){
                                user = ut.empty()
                                        ? Optional.ofNullable(EMPTY_USER.poll())
                                        : Optional.ofNullable(NOT_EMPTY_USER.poll());
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
                                    () -> new IllegalStateException("Can't find user after 30 sec")
                            );
                        }

                );
    }


    @Override
    public void afterEach(ExtensionContext context) throws Exception {

    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(),UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(),StaticUser.class);
    }
}
