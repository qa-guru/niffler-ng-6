package guru.qa.niffler.jupiter.extantion;

import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;

import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.UUID;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    System.out.println(anno);
                    if (anno.categories().length>0) {
                        System.out.println("#############Catigory Not NULL############");
                       AnnotationSupport.findAnnotation(anno.categories().getClass(), Category.class)


                                .ifPresent(annoCatigory -> {
                                    CategoryJson category = new CategoryJson(
                                            null,
                                            RandomDataUtils.randomName(),
                                            anno.username(),
                                            annoCatigory.archived()
                                    );
                                    System.out.println("#############Catigory Not NULL############");
                                    System.out.println(category.name());
                                    CategoryJson created = categoriesApiClient.addCategory(category);
                                    if (annoCatigory.archived()) {
                                        CategoryJson archivedCategory = new CategoryJson(
                                                created.id(),
                                                created.name(),
                                                created.username(),
                                                true
                                        );
                                        created = categoriesApiClient.updateCategory(archivedCategory);
                                    }
                                    System.out.println(created.name());
                                    context.getStore(NAMESPACE).put(
                                            context.getUniqueId(),
                                            created
                                    );
                                });
                    }

                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category =
                context.getStore(NAMESPACE).get(context.getUniqueId(),
                        CategoryJson.class);
        if (!category.archived()) {
            categoriesApiClient.updateCategory(new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            ));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}