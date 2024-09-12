package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class CreateCategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    private final SpendApiClient categoryApiClient = new SpendApiClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    String randomCategoryName = faker.commerce().department();
                    CategoryJson category = new CategoryJson(
                            null,
                            randomCategoryName,
                            anno.username(),
                            false // Niffler-spend не поддерживает создание сразу архивной категории
                    );
                    // Отправляем запрос на создание категории
                    CategoryJson createdCategory = categoryApiClient.addCategory(category);

                    // Если категория должна быть архивной, отправляем второй запрос на обновление
                    if (anno.archived()) {
                        CategoryJson archivedCategory = new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        );
                        createdCategory = categoryApiClient.updateCategory(archivedCategory);
                    }

                    // Сохраняем уже созданную или обновленную категорию в контекст
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdCategory
                    );
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        // Если категория не архивирована, архивируем её после теста
        if (category.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true // Архивируем категорию
            );
            categoryApiClient.updateCategory(archivedCategory);
        }
    }
}