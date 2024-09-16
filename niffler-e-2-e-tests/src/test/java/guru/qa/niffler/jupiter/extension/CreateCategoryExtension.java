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

                    CategoryJson categoryJson = categoryApiClient.addCategory(createdCategory);

                    if (anno.archived()) {
                        CategoryJson updateCategory = new CategoryJson(
                                categoryJson.id(),
                                categoryJson.name(),
                                categoryJson.username(),
                                true
                        );

                        createdCategory = categoryApiClient.updateCategory(updateCategory);
                    }

                    context.getStore(CATEGORY_NAMESPACE).put(
                            context.getUniqueId(),
                            createdCategory
                    );
                });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson category = context.getStore(CATEGORY_NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        CategoryJson categoryJson = new CategoryJson(
                category.id(),
                category.name(),
                category.username(),
                true
        );

        categoryApiClient.updateCategory(categoryJson);

    }
}
