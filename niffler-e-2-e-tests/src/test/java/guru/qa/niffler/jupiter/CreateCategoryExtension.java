package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.UUID;

public class CreateCategoryExtension implements BeforeEachCallback {

    public static final  ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno ->{
                    CategoryJson category = new CategoryJson(
                           null,
                            UUID.randomUUID().toString().substring(6),
                            anno.username(),
                            anno.archived()
                    );
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            categoriesApiClient.addCategory(category)
                    );
                });

    }
}
