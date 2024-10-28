package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.db.impl.springJdbc.SpendDbClientSpringJdbc;
import guru.qa.niffler.service.db.impl.springJdbc.UsersDbClientSpringJdbc;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CreateNewUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateNewUserExtension.class);
    private static final UserMapper userMapper = new UserMapper();
    private UsersClient usersClient = new UsersDbClientSpringJdbc();
    private SpendClient spendClient = new SpendDbClientSpringJdbc();

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class) && parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(parameter -> {

                    var parameterName = parameter.getName();
                    var parameterAnno = parameter.getAnnotation(CreateNewUser.class);
                    UserJson user = usersClient.createUser(
                            userMapper.updateFromAnno(
                                    UserUtils.generateUser(),
                                    parameterAnno));

                    @SuppressWarnings("unchecked")
                    Map<String, UserJson> usersMap = ((Map<String, UserJson>) context.getStore(NAMESPACE)
                            .getOrComputeIfAbsent(context.getUniqueId(), map -> new HashMap<>()));
                    usersMap.put(parameterName, user);

                    log.info("Created new user: {}", user);

                });

    }

    @Override
    public void afterEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class)
                        && parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(
                        parameter -> {
                            var parameterName = parameter.getName();

                            @SuppressWarnings("unchecked")
                            Map<String, UserJson> usersMap = (Map<String, UserJson>) context
                                    .getStore(CreateNewUserExtension.NAMESPACE)
                                    .get(context.getUniqueId());

                            UserJson user = usersMap.get(parameterName);

                            spendClient.findAllByUsername(user.getUsername()).forEach(spendClient::remove);
                            spendClient.findAllCategoriesByUsername(user.getUsername()).forEach(spendClient::removeCategory);
                            usersClient.removeUser(user);

                        }
                );

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        @SuppressWarnings("unchecked")
        Map<String, UserJson> usersMap = (Map<String, UserJson>) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);
        return usersMap.get(parameterContext.getParameter().getName());
    }

}