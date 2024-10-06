package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.ex.CategoryStatusException;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.utils.CategoryUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.niffler.helper.StringHelper.*;

@Slf4j
public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final CategoryApiClient categoryApiClient = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {

                    UserModel user = context
                            .getStore(CreateNewUserExtension.NAMESPACE)
                            .get(context.getUniqueId(), UserModel.class);

                    log.info("User from CreateNewUser {}", user);

                    var username = getUsernameOrThrowException(
                            context
                                    .getStore(CreateNewUserExtension.NAMESPACE)
                                    .get(context.getUniqueId(), UserModel.class),
                            anno.username());

                    // If active category with same name exists -> throw exception
                    getUserCategories(username, anno.name(), true).ifPresent(category -> {
                        throw new CategoryStatusException("Active category [%s] already exists".formatted(anno.name()));
                    });

                    CategoryJson category = new CategoryMapper()
                            .updateFromAnno(CategoryUtils.generate().setUsername(username), anno);

                    var isArchive = category.isArchived();
                    category = categoryApiClient.createCategory(category);
                    if (isArchive) categoryApiClient.updateCategory(category.setArchived(true));

                    context.getStore(NAMESPACE)
                            .put(context.getUniqueId(), category);

                    log.info("Category successfully created: {}", category);

                });

    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {

                    var category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

                    if (!category.isArchived()) categoryApiClient.updateCategory(category.setArchived(true));
                    log.info("Successfully updated status to [archived] for category: {}", category.getName());

                });

    }

    private Optional<CategoryJson> getUserCategories(@NonNull String username, @NonNull String categoryName, boolean excludeArchived) {
        return categoryApiClient.getAllCategories(username, excludeArchived).stream()
                .filter(category -> category.getName().equals(categoryName))
                .findFirst();
    }

    private String getUsernameOrThrowException(UserModel user, String annoUsername) {

        if (user == null) user = new UserModel();
        var username = user.getUsername();

        if (isNullOrEmpty(username) && isNullOrEmpty(annoUsername)) {
            throw new IllegalArgumentException("Username should contains in @Spending or should add @CreateNewUser on test");
        } else if (isNotNullOrEmpty(username) && isNotNullOrEmpty(annoUsername) && !user.getUsername().equals(annoUsername)) {
            throw new IllegalArgumentException("You can not set different usernames in @Spending(username=[%s]) and @CreateNewUser(username=[%s]) annotations"
                    .formatted(username, annoUsername));
        } else {
            return isNotNullOrBlank(username) ? username : annoUsername;
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }


}