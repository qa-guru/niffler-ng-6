package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.AddCategory;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class AddCategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(AddCategoryExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), AddCategory.class)
                .ifPresent(anno ->{
                    String categoryName = faker.backToTheFuture().quote();
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            categoryName,
                            anno.username(),
                            false
                    );

                    CategoryJson createCategory = spendApiClient.addCategory(categoryJson);

                    if (anno.isCategoryArchive()) {
                        CategoryJson archivedCategory = new CategoryJson(
                                createCategory.id(),
                                createCategory.name(),
                                createCategory.username(),
                                true
                        );
                        createCategory = spendApiClient.updateCategory(archivedCategory);
                    }

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createCategory);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(AddCategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if (categoryJson.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    true
            );
            spendApiClient.updateCategory(archivedCategory);
        }
    }
}
