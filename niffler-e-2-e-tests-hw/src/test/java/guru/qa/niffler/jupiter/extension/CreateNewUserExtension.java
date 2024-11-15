package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UserdataClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.api.impl.SpendApiClientImpl;
import guru.qa.niffler.service.api.impl.UserdataApiClientImpl;
import guru.qa.niffler.service.api.impl.UsersApiClientImpl;
import guru.qa.niffler.service.db.SpendDbClient;
import guru.qa.niffler.service.db.UsersDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ParametersAreNonnullByDefault
public class CreateNewUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateNewUserExtension.class);
    private static final UserMapper userMapper = new UserMapper();
    private final UsersClient usersClient = new UsersApiClientImpl();
    private final UserdataClient userdataClient = new UserdataApiClientImpl();
    private final SpendClient spendClient = new SpendApiClientImpl();

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class) && parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(parameter -> {

                    UserJson user = usersClient.createUser(
                            userMapper.updateFromAnno(
                                    UserUtils.generateUser(),
                                    parameter.getAnnotation(CreateNewUser.class)));

                    @SuppressWarnings("unchecked")
                    Map<String, UserJson> usersMap = ((Map<String, UserJson>) context.getStore(NAMESPACE)
                            .getOrComputeIfAbsent(context.getUniqueId(), map -> new HashMap<>()));
                    usersMap.put(parameter.getName(), user);

                    log.info("Created new user: {}", user);

                });

    }

    @Override
    public void afterEach(ExtensionContext context) {

        boolean isDbClient = usersClient instanceof UsersDbClient &&
                userdataClient instanceof UsersDbClient &&
                spendClient instanceof SpendDbClient;

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateNewUser.class)
                        && parameter.getType().isAssignableFrom(UserJson.class))
                .forEach(
                        parameter -> {

                            @SuppressWarnings("unchecked")
                            Map<String, UserJson> usersMap = (Map<String, UserJson>) context.getStore(NAMESPACE)
                                    .get(context.getUniqueId());

                            UserJson user = usersMap.get(parameter.getName());

                            user.getTestData().getFriends().forEach(
                                    friend -> {
                                        userdataClient.unfriend(user, friend);
                                        if (isDbClient) {
                                            usersClient.removeUser(friend);
                                        }
                                    }
                            );

                            user.getTestData().getIncomeInvitations().forEach(
                                    requester -> {
                                        userdataClient.declineInvitation(requester, user);
                                        if (isDbClient) {
                                            usersClient.removeUser(requester);
                                        }
                                    }
                            );

                            user.getTestData().getOutcomeInvitations().forEach(
                                    addressee -> {
                                        userdataClient.declineInvitation(user, addressee);
                                        if(isDbClient) {
                                            usersClient.removeUser(addressee);
                                        }
                                    }
                            );

                            spendClient.findAllByUsername(user.getUsername()).forEach(spendClient::remove);
                            if (isDbClient) {
                                spendClient.findAllCategoriesByUsername(user.getUsername())
                                        .forEach(spendClient::removeCategory);
                                usersClient.removeUser(user);
                            } else {
                                spendClient.findAllCategoriesByUsername(user.getUsername())
                                        .forEach(category ->
                                                spendClient.updateCategory(category.setArchived(true)));
                            }


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