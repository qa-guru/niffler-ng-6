package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.RegisterModelMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CreateNewUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateNewUserExtension.class);
    private final AuthApiClient authApiClient  = new AuthApiClient();
    private final RegisterModelMapper registerModelMapper  = new RegisterModelMapper();
    private final UserDbClient userDbClient = new UserDbClient();
    private final CategoryDbClient categoryDbClient = new CategoryDbClient();
    private final SpendDbClient spendDbClient = new SpendDbClient();
    private final UserMapper userMapper = new UserMapper();

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class) && parameter.getType().isAssignableFrom(UserModel.class))
                .forEach(parameter -> {

                    var parameterName = parameter.getName();
                    var parameterAnno = parameter.getAnnotation(CreateNewUser.class);
                    UserModel user = userMapper.updateFromAnno(UserUtils.generateValidUser(), parameterAnno);
                    authApiClient.register(registerModelMapper.fromUserModel(user));
                    user.setId(userDbClient.findByUsername(user.getUsername()).get().getId());

                    @SuppressWarnings("unchecked")
                    Map<String, UserModel> usersMap = ((Map<String, UserModel>) context.getStore(NAMESPACE)
                            .getOrComputeIfAbsent(context.getUniqueId(), map -> new HashMap<>()));
                    usersMap.put(parameterName, user);

                    log.info("Created new user: {}", user);

                });

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

                            spendings.forEach(spend -> spendDbClient.delete(spend.getId()));
                            categories.forEach(category -> categoryDbClient.delete(category.getId()));
                            userDbClient.delete(user.getId());

                        }
                );

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserModel.class);
    }

    @Override
    public UserModel resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        @SuppressWarnings("unchecked")
        Map<String, UserModel> usersMap = (Map<String, UserModel>) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);
        return usersMap.get(parameterContext.getParameter().getName());
    }

}