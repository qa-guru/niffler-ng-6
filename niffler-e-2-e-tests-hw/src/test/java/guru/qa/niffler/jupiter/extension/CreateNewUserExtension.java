package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.RegisterModelMapper;
import guru.qa.niffler.mapper.UserModelMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CreateNewUserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateNewUserExtension.class);
    private RegisterModelMapper registerModelMapper = new RegisterModelMapper();
    private AuthApiClient authApiClient = new AuthApiClient();
    private UserModelMapper userMapper = new UserModelMapper();

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class) && parameter.getType().isAssignableFrom(UserModel.class))
                .forEach(parameter -> {

                    var parameterName = parameter.getName();
                    var parameterAnno = parameter.getAnnotation(CreateNewUser.class);
                    UserModel user = userMapper.updateFromAnno(UserUtils.generateValidUser(), parameterAnno);
                    authApiClient.register(registerModelMapper.fromUserModel(user));

                    @SuppressWarnings("unchecked")
                    Map<String, UserModel> usersMap = ((Map<String, UserModel>) context.getStore(NAMESPACE)
                            .getOrComputeIfAbsent(context.getUniqueId(), map -> new HashMap<>()));
                    usersMap.put(parameterName, user);

                    log.info("Created new user: {}", user);

                });

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