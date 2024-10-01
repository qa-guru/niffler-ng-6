package guru.qa.niffler.jupiter.myextensions;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import wiremock.org.apache.commons.lang3.time.StopWatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUserExtended(String username, String password, String friend, String incomeFriendRequest,
                                     String outcomeFriendRequest) {
    }

    private static final Queue<StaticUserExtended> EMPTY_EXTENDED_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUserExtended> WITH_FRIEND_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUserExtended> WITH_INCOME_FRIEND_REQUEST_EXTENDED_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUserExtended> WITH_OUTCOME_FRIEND_REQUEST_EXTENDED_USERS = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_EXTENDED_USERS.add(new StaticUserExtended("solo", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUserExtended("duck", "12345", "dog", null, null));
        WITH_INCOME_FRIEND_REQUEST_EXTENDED_USERS.add(new StaticUserExtended("duck2", "12345", null, "cat", null));
        WITH_OUTCOME_FRIEND_REQUEST_EXTENDED_USERS.add(new StaticUserExtended("cat", "12345", null, null, "duck2"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserTypeExtended {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIENDS, WITH_INCOME_FRIEND_REQUEST, WITH_OUTCOME_FRIEND_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Map<UserTypeExtended, StaticUserExtended> muEx = new HashMap();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserTypeExtended.class))
                .map(p -> p.getAnnotation(UserTypeExtended.class))
                .forEach(ut -> {
                    Optional<StaticUserExtended> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        switch (ut.value()) {
                            case WITH_FRIENDS: {
                                user = Optional.ofNullable(WITH_FRIEND_USERS.poll());
                                break;
                            }
                            case WITH_INCOME_FRIEND_REQUEST: {
                                user = Optional.ofNullable(WITH_INCOME_FRIEND_REQUEST_EXTENDED_USERS.poll());
                                break;
                            }
                            case WITH_OUTCOME_FRIEND_REQUEST: {
                                user = Optional.ofNullable(WITH_OUTCOME_FRIEND_REQUEST_EXTENDED_USERS.poll());
                                break;
                            }
                            case EMPTY:
                            default: {
                                user = Optional.ofNullable(EMPTY_EXTENDED_USERS.poll());
                            }
                        }
                    }
                    user.ifPresentOrElse(
                            u -> {
                                muEx.put(ut, u);
                            },
                            () -> new IllegalStateException("Can't find user after 30 sec")
                    );
                });
        context.getStore(NAMESPACE)
                .put(context.getUniqueId(),
                        muEx);

        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserTypeExtended, StaticUserExtended> muEx = (Map<UserTypeExtended, StaticUserExtended>) context.getStore(NAMESPACE).getOrComputeIfAbsent(context.getUniqueId(), key -> new HashMap<>());
        muEx.forEach((ut, u) -> {
            switch (ut.value()) {
                case WITH_FRIENDS: {
                    WITH_FRIEND_USERS.add(u);
                    break;
                }
                case WITH_INCOME_FRIEND_REQUEST: {
                    WITH_INCOME_FRIEND_REQUEST_EXTENDED_USERS.add(u);
                    break;
                }
                case WITH_OUTCOME_FRIEND_REQUEST: {
                    WITH_OUTCOME_FRIEND_REQUEST_EXTENDED_USERS.add(u);
                    break;
                }
                case EMPTY:
                default: {
                    EMPTY_EXTENDED_USERS.add(u);
                }
            }
        });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUserExtended.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserTypeExtended.class);
    }

    @Override
    public StaticUserExtended resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserTypeExtended, StaticUserExtended> muEx = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        StaticUserExtended staticUserExtended = null;
        if (!muEx.isEmpty()) {
            staticUserExtended = muEx.get(parameterContext.getParameter().getAnnotation(UserTypeExtended.class));
        } else
            new IllegalStateException("Can't find user");
        return staticUserExtended;
    }
}
