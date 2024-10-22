package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.enums.TypeEnum;
import guru.qa.niffler.jupiter.annotation.UserType;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UserQueueExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace USER_QUEUE_NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

  public record StaticUser(
      String userName,
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
    EMPTY_USERS.add(new StaticUser("maxTest1", "max", null, null, null));
    WITH_FRIEND_USERS.add(new StaticUser("maxTest2", "max", "maxTest3", null, null));
    WITH_INCOME_REQUEST_USERS.add(new StaticUser("maxTest3", "max", "maxTest2", "maxTest1", null));
    WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("maxTest4", "max", null, null, "max"));
  }

  private Queue<StaticUser> getQueueByUserType(TypeEnum userType) {
    switch (userType) {
      case EMPTY -> {
        return EMPTY_USERS;
      }
      case WITH_FRIEND -> {
        return WITH_FRIEND_USERS;
      }
      case WITH_INCOME_REQUEST -> {
        return WITH_INCOME_REQUEST_USERS;
      }
      case WITH_OUTCOME_REQUEST -> {
        return WITH_OUTCOME_REQUEST_USERS;
      }
      default -> throw new IllegalArgumentException("Unsupported user type: " + userType);
    }
  }

  private Map<UserType, StaticUser> getContextMap(ExtensionContext context) {
    return ((Map<UserType, StaticUser>) context.getStore(USER_QUEUE_NAMESPACE)
        .getOrComputeIfAbsent(context.getUniqueId(), key -> new HashMap<UserType, StaticUser>()));
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
        .toList().stream()
        .map(p -> p.getAnnotation(UserType.class))
        .forEach(userType -> {
          Optional<StaticUser> user = Optional.empty();
          StopWatch sw = StopWatch.createStarted();
          while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
            user = Optional.ofNullable(getQueueByUserType(userType.value()).poll());
          }
          user.ifPresentOrElse(
              userFromStream -> getContextMap(context).put(userType, userFromStream),
              () -> {
                throw new IllegalStateException("Can't find user after 30 sec");
              }
          );
        });
    Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Map<UserType, StaticUser> map = getContextMap(context);
    if (map != null) {
      for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
        getQueueByUserType(e.getKey().value()).add(e.getValue());
      }
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
      ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    UserType userAnnotationType = parameterContext.getParameter().getAnnotation(UserType.class);
    StaticUser userFromParam = getContextMap(extensionContext).get(userAnnotationType);
    if (userFromParam != null) {
      return userFromParam;
    } else {
      throw new ParameterResolutionException(String.format("Пользователь с типом: %s не найден", userAnnotationType.value()));
    }
  }
}
