package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.helper.CollectionsHelper;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static guru.qa.niffler.helper.CollectionsHelper.isNotNullOrEmpty;

@Slf4j
public class SpendingExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final CategoryApiClient categoryApiClient = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class)
                        && parameter.getType().isAssignableFrom(UserModel.class))
                .forEach(
                        parameter -> {
                            var parameterName = parameter.getName();
                            var userAnno = parameter.getAnnotation(CreateNewUser.class);
                            if (userAnno.spendings().length > 0) {

                                @SuppressWarnings("unchecked")
                                Map<String, UserModel> usersMap = (Map<String, UserModel>) context
                                        .getStore(CreateNewUserExtension.NAMESPACE)
                                        .get(context.getUniqueId());
                                UserModel user = usersMap.get(parameterName);

                                Spending spendAnno = userAnno.spendings()[0];
                                List<SpendJson> spendings = new ArrayList<>();

                                SpendJson spend = new SpendMapper()
                                        .updateFromAnno(
                                                SpendUtils.generate().setUsername(user.getUsername()),
                                                spendAnno
                                        );
                                spend.getCategory().setUsername(user.getUsername());
                                spendings.add(spendApiClient.createNewSpend(spend));

                                context.getStore(NAMESPACE).put(
                                        context.getUniqueId(),
                                        usersMap.put(parameterName, user.setSpendings(spendings))
                                );

                                log.info("Created new spendings for user = [{}]: {}", user.getUsername(), user.getSpendings());
                            }

                        }

                );

    }

    @Override
    public void afterEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class)
                        && parameter.getType().isAssignableFrom(UserModel.class))
                .forEach(
                        parameter -> {
                            var parameterName = parameter.getName();

                            @SuppressWarnings("unchecked")
                            Map<String, UserModel> usersMap = (Map<String, UserModel>) context
                                    .getStore(CreateNewUserExtension.NAMESPACE)
                                    .get(context.getUniqueId());

                            UserModel user = usersMap.get(parameterName);

                            List<SpendJson> spendings = user.getSpendings();
                            List<CategoryJson> categories = user.getCategories();

                            if (isNotNullOrEmpty(spendings) && isNotNullOrEmpty(categories)) {
                                spendApiClient.deleteSpends(
                                        user.getUsername(),
                                        spendings.stream().map(spend -> spend.getId().toString()).toList());

                                var spendCategory = spendings.getFirst().getCategory();
                                if (!spendCategory.equals(categories.getFirst()))
                                    categoryApiClient.updateCategory(spendCategory.setArchived(true));
                            }
                        }
                );

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }

}