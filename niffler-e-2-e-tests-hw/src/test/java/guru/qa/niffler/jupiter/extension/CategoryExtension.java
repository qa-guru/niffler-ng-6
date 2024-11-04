package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.api.SpendApiClient;
import guru.qa.niffler.service.db.impl.springJdbc.SpendDbClientSpringJdbc;
import guru.qa.niffler.utils.CategoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@ParametersAreNonnullByDefault
public class CategoryExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendClient spendClient = new SpendDbClientSpringJdbc();

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class)
                        && parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(parameter -> {

                    var parameterName = parameter.getName();
                    var userAnno = parameter.getAnnotation(CreateNewUser.class);

                    if (userAnno.categories().length > 0) {

                        var activeCategoriesCount = Arrays.stream(userAnno.categories())
                                .filter(category -> !category.isArchived() || category.generateIsArchived())
                                .count();
                        if ((spendClient instanceof SpendApiClient) && activeCategoriesCount > 8) {
                            throw new IllegalArgumentException("More than 8 @Categories annotations in " + CreateNewUser.class.getSimpleName());
                        }

                        @SuppressWarnings("unchecked")
                        Map<String, UserJson> usersMap = ((Map<String, UserJson>) context
                                .getStore(CreateNewUserExtension.NAMESPACE)
                                .get(context.getUniqueId()));
                        var user = usersMap.get(parameterName);
                        var username = user.getUsername();
                        if (userAnno.categories().length > 0) {

                            List<CategoryJson> categories = new ArrayList<>();
                            Arrays.stream(userAnno.categories()).forEach(categoryAnno -> {

                                CategoryJson category = new CategoryMapper()
                                        .updateDtoFromAnno(
                                                CategoryUtils.generateForUser(username),
                                                categoryAnno);

                                category = spendClient.createCategory(category);
                                categories.add(category);

                                log.info("Created new category: {}", category);

                            });


                            usersMap.put(
                                    parameterName,
                                    user.setTestData(user.getTestData().setCategories(categories)));

                            context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);

                            log.info("Created new categories for user = [{}]: {}",
                                    user.getUsername(),
                                    user.getTestData().getCategories());
                        }
                    }
                });

    }

}