package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        String nameCategory = faker.country().name();

        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson categoryJson = new CategoryJson(
                            null,
                            nameCategory,
                            anno.username(),
                            false
                    );
                   CategoryJson createCategoryJson = spendApiClient.addCategory(categoryJson);
                   if(anno.archived()) {
                       CategoryJson archivedCategoryJson = new CategoryJson(
                               createCategoryJson.id(),
                               createCategoryJson.name(),
                               createCategoryJson.username(),
                               true
                       );
                       createCategoryJson = spendApiClient.updateCategory(archivedCategoryJson);
                   }
                   context.getStore(NAMESPACE).put(context.getUniqueId(), createCategoryJson);
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context){
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if(categoryJson.archived()) {
            CategoryJson archivedCategoryJson = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    true
            );
            spendApiClient.updateCategory(archivedCategoryJson);
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
