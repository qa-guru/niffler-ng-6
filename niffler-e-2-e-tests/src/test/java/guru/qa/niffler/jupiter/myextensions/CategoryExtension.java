package guru.qa.niffler.jupiter.myextensions;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.myannotations.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.myapis.SpendApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Faker faker = new Faker();

        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    String categoryName = "";
                    if (anno.title().equals("")) {
                        categoryName = faker.beer().name();
                    } else {
                        categoryName = anno.title();
                    }
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            categoryName,
                            anno.username(),
                            false);
                    CategoryJson newCategory = spendApiClient.addCategory(categoryJson);
                    if (anno.archived()) {
                        CategoryJson archCategory = new CategoryJson(
                                newCategory.id(),
                                newCategory.name(),
                                newCategory.username(),
                                true
                        );
                        newCategory = spendApiClient.updateCategory(archCategory);
                    }
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            newCategory);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!category.archived()) {
            spendApiClient.updateCategory(new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true));
        }
    }
}
