package guru.qa.niffler.jupiter.category;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {

  public static final Namespace NAMESPACE = Namespace.create(CategoryExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(
            anno -> {
              CategoryJson categoryJson = new CategoryJson(
                  null,
                  anno.name(),
                  anno.username(),
                  anno.isArchived()
              );
              final CategoryJson createdSpend = spendApiClient.createCategory(categoryJson);
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
    return extensionContext.getStore(CategoryExtension.NAMESPACE)
        .get(extensionContext.getUniqueId(),
            SpendJson.class);
  }
}