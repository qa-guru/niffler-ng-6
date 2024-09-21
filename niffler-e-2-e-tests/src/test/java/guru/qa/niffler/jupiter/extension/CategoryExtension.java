package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    private final Faker faker = new Faker();
    private final SpendApiClient spendApiClient = new SpendApiClient();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    String categoryName;

                    if (anno.title().isEmpty()) {
                        categoryName = faker.animal().name();
                    } else {
                        categoryName = anno.title();
                    }

                    CategoryJson category = new CategoryJson(
                            null,
                            categoryName,
                            anno.username(),
                            false
                    );

                    CategoryJson createdCategory = spendApiClient.addCategory(category);

                    if (anno.archived()) {
                        CategoryJson archivedCategory = new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        );
                        createdCategory = spendApiClient.updateCategory(archivedCategory);
                    }

                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdCategory
                    );
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
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        CategoryJson archiveCategory = new CategoryJson(
                category.id(),
                category.name(),
                category.username(),
                true
        );
        spendApiClient.updateCategory(archiveCategory);
    }
}
