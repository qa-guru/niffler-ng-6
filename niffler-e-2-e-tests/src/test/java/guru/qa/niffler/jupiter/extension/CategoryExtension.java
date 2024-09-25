package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.categories())) {
            Category categoryAnno = userAnno.categories()[0];
            CategoryJson category = new CategoryJson(
                null,
                randomCategoryName(),
                userAnno.username(),
                categoryAnno.archived()
            );

            CategoryJson created = spendApiClient.createCategory(category);
            if (categoryAnno.archived()) {
              CategoryJson archivedCategory = new CategoryJson(
                  created.id(),
                  created.name(),
                  created.username(),
                  true
              );
              created = spendApiClient.updateCategory(archivedCategory);
            }

            context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                created
            );
          }
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    if (category != null && !category.archived()) {
      category = new CategoryJson(
          category.id(),
          category.name(),
          category.username(),
          true
      );
      spendApiClient.updateCategory(category);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}
