package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.UUID;

public class CreateCategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    public static final  ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno ->{
                    CategoryJson category = new CategoryJson(
                           null,
                            UUID.randomUUID().toString().substring(0,6),
                            anno.username(),
                            anno.archived()
                    );

                    CategoryJson created = categoriesApiClient.addCategory(category);
                    if (anno.archived()) {
                        CategoryJson archivedCategory = new CategoryJson(
                                created.id(),
                                created.name(),
                                created.username(),
                                true
                        );
                        created = categoriesApiClient.updateCategory(archivedCategory);
                    }
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            created
                    );
                });

    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category =
                context.getStore(NAMESPACE).get(context.getUniqueId(),
                        CategoryJson.class);
        if (!category.archived()) {
// создаем объект с archived = true
            categoriesApiClient.updateCategory(new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            ));
        }
    }

}
