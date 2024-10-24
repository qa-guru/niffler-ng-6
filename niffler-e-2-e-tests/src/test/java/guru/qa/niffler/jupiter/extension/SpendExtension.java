package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendExtension implements BeforeEachCallback, ParameterResolver {

  public static final Namespace SPEND_NAMESPACE = ExtensionContext.Namespace.create(SpendExtension.class);
  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(
            anno -> {
              if (anno.spendings().length != 0) {
                Spending spending = anno.spendings()[0];
                SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    new CategoryJson(null, spending.category(), anno.username(), false),
                    spending.currency(),
                    spending.amount(),
                    spending.description(),
                    anno.username()
                );
                final SpendJson createdSpend = spendDbClient.createSpend(spendJson);
                context.getStore(SPEND_NAMESPACE)
                    .put(context.getUniqueId(), createdSpend);
              }
            }
        );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext
        .getParameter()
        .getType()
        .isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(SpendExtension.SPEND_NAMESPACE)
        .get(extensionContext.getUniqueId(),
            SpendJson.class);
  }
}