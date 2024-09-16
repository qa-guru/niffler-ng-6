package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class CreateCategoryExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace CATEGORY_NAMESPACE =
            ExtensionContext.Namespace.create(CreateCategoryExtension.class);
    private static final Faker faker = new Faker();

    SpendApiClient categoryApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson createdCategory = new CategoryJson(
                            null,
                            anno.name().isEmpty() ? faker.company().name() : anno.name(),
                            anno.username(),
                            false
                    );

                    CategoryJson category = categoryApiClient.addCategory(createdCategory);

                    if (anno.archived()) {
                        CategoryJson updatedCategory = new CategoryJson(
                                category.id(),
                                category.name(),
                                category.username(),
                                true
                        );
                        category = categoryApiClient.updateCategory(updatedCategory);
                    }

                    context.getStore(CATEGORY_NAMESPACE).put(
                            context.getUniqueId(),
                            category
                    );
                });
    }


    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson categoryCtx = context.getStore(CATEGORY_NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        CategoryJson category = new CategoryJson(
                categoryCtx.id(),
                categoryCtx.name(),
                categoryCtx.username(),
                true
        );

        categoryApiClient.updateCategory(category);

    }
}
