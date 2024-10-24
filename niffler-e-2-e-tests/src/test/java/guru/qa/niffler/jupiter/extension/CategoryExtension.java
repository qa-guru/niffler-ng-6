package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryDbClient;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final Namespace CATEGORY_NAMESPACE = Namespace.create(CategoryExtension.class);
  private final CategoryDbClient categoryDbClient = new CategoryDbClient();
  private final Faker faker = new Faker();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(
            anno -> {
              if (anno.categories().length != 0) {
                CategoryJson categoryJson = new CategoryJson(
                    null,
                    "Тестовая " + randomCategoryName(),
                    anno.username(),
                    false
                );
                CategoryJson createdCategory = categoryDbClient.createCategory(categoryJson);
                context.getStore(CATEGORY_NAMESPACE).put(context.getUniqueId(), createdCategory);
              }
            }
        );
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(
            anno -> {
              CategoryJson categoryJson = context.getStore(CategoryExtension.CATEGORY_NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
              if (categoryJson != null) {
                CategoryJson categoryJsonForPatching = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    true
                );
                categoryDbClient.updateCategory(categoryJsonForPatching);
              }
            }
        );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext
        .getParameter()
        .getType()
        .isAssignableFrom(CategoryJson.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(CategoryExtension.CATEGORY_NAMESPACE)
        .get(extensionContext.getUniqueId(),
            CategoryJson.class);
  }
}