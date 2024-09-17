package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(User.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (userAnno.categories().length > 0) {
                        Category anno = userAnno.categories()[0];
                        // Создаем категорию с рандомным именем
                        String randomName = randomCategoryName();
                        CategoryJson category = new CategoryJson(
                                null,
                                randomName,
                                userAnno.username(),
                                false // Изначально категория не архивная
                        );

                        // Создаем категорию через API
                        CategoryJson createdCategory = spendApiClient.addCategory(category);

                        // Если категория должна быть архивной, архивируем её
                        if (anno.archived()) {
                            CategoryJson archivedCategory = new CategoryJson(
                                    createdCategory.id(),
                                    createdCategory.name(),
                                    createdCategory.username(),
                                    true
                            );
                            createdCategory = spendApiClient.updateCategory(archivedCategory);
                        }

                        // Сохраняем категорию в контексте
                        context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);

                    }
                });
    }


    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            // Если категория не архивирована, архивируем её после теста
            if (!category.archived()) {
                CategoryJson archivedCategory = new CategoryJson(
                        category.id(),
                        category.name(),
                        category.username(),
                        true // Архивируем категорию
                );
                spendApiClient.updateCategory(archivedCategory);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
