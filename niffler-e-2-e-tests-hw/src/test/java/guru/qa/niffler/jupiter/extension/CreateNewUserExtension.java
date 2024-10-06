package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.RegisterModelMapper;
import guru.qa.niffler.mapper.UserModelMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

@Slf4j
public class CreateNewUserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateNewUserExtension.class);
    private final RegisterModelMapper registerModelMapper = new RegisterModelMapper();
    private final AuthApiClient authApiClient = new AuthApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), CreateNewUser.class)
                .ifPresent(anno -> {
                    UserModel user = new UserModelMapper().updateFromAnno(UserUtils.generateValidUser(), anno);

                    authApiClient.register(registerModelMapper.fromUserModel(user));
                    context.getStore(NAMESPACE)
                            .put(context.getUniqueId(), user);

                    log.info("Created new user: {}", user);

                });

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserModel.class);
    }

    @Override
    public UserModel resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserModel.class);
    }
}