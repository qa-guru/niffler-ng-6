package guru.qa.niffler.jupiter.extensions;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.faker.RandomDataUtils;
import guru.qa.niffler.jupiter.annotations.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        String categoryName = RandomDataUtils.randomCategoryName();
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.categories().length > 0) {
                        CategoryJson category = new CategoryJson(
                                null,
                                categoryName,
                                anno.username(),
                                false
                        );
                        CategoryJson newCategory = spendApiClient.addCategory(category);
                        if (anno.categories()[0].archived()) {
                            CategoryJson archivedCategory = new CategoryJson(
                                    newCategory.id(),
                                    newCategory.name(),
                                    newCategory.username(),
                                    true
                            );
                            newCategory = spendApiClient.updateCategory(archivedCategory);
                        }
                        context.getStore(NAMESPACE).put(context.getUniqueId(), newCategory);
                    }
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson annotationCategory = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (annotationCategory != null && annotationCategory.archived()) {
            CategoryJson newCategory = new CategoryJson(
                    annotationCategory.id(),
                    annotationCategory.name(),
                    annotationCategory.username(),
                    true
            );
            spendApiClient.updateCategory(newCategory);
        }
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
