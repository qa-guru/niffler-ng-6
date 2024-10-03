package guru.qa.niffler.jupiter.spend;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.submodel.CategoryJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendExtension implements BeforeEachCallback, ParameterResolver {

  public static final Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spend.class)
        .ifPresent(
            anno -> {
              SpendJson spendJson = new SpendJson(
                  null,
                  new Date(),
                  new CategoryJson(null, anno.category(), anno.username(), false),
                  anno.currency(),
                  anno.amount(),
                  anno.description(),
                  anno.username()
              );
              final SpendJson createdSpend = spendApiClient.createSpend(spendJson);
              context.getStore(NAMESPACE)
                  .put(context.getUniqueId(), createdSpend);
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
    return extensionContext.getStore(SpendExtension.NAMESPACE)
        .get(extensionContext.getUniqueId(),
            SpendJson.class);
  }
}