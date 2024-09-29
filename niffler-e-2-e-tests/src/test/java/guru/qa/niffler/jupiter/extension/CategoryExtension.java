package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;


public class CategoryExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    CategoryDbClient categoryDbClient = new CategoryDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.categories().length != 0) {
                        Category categoryAnno = anno.categories()[0];

                        String categoryName = categoryAnno.name().isEmpty()
                                              ? RandomDataUtils.randomCategoryName()
                                              : categoryAnno.name();

                        CategoryEntity category = new CategoryEntity();
                        category.setName(categoryName);
                        category.setUsername(anno.username());
                        category.setUsername(anno.username());
                        category.setArchived(categoryAnno.archived());

                        CategoryJson categoryJson = categoryDbClient.createCategory(category);
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                categoryJson
                        );
                    }
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
    public void afterEach(ExtensionContext context) {
        var category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category == null) return;

        categoryDbClient.deleteCategory(CategoryEntity.fromJson(category));
    }
}
