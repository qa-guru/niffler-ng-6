package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.db.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

public class CategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (ArrayUtils.isNotEmpty(anno.categories())) {
                        List<CategoryJson> result = new ArrayList<>();
                        UserJson user = context.getStore(UserExtension.NAMESPACE)
                                .get(context.getUniqueId(), UserJson.class);
                        for (Category categoryAnnotation : anno.categories()) {
                            final String categoryName = "".equals(categoryAnnotation.name())
                                    ? RandomDataUtils.randomName()
                                    : categoryAnnotation.name();
                            CategoryJson category = new CategoryJson(
                                    null,
                                    categoryName,
                                    user != null ? user.username() : anno.username(),
                                    false
                            );
                            CategoryJson createdCategory = spendClient.createCategory(category);
                            result.add(createdCategory);
                        }
                        if (user != null) {
                            user.testData().categories().addAll(result);
                        } else {
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    result
                            );
                        }

                    }
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UserJson user = context.getStore(UserExtension.NAMESPACE)
                .get(context.getUniqueId(), UserJson.class);
        List<CategoryJson> categories = user != null
                ? user.testData().categories()
                : context.getStore(NAMESPACE).get(context.getUniqueId(), List.class);
        for (CategoryJson category : categories) {
            spendClient.deleteCategory(category);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (CategoryJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
                .toArray();
    }


}