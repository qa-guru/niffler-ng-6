package guru.qa.niffler.jupiter.extension;


import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UserClient;

import guru.qa.niffler.service.db.UserDbClient;
import guru.qa.niffler.service.rest.UserRestClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;


public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

    private final UserClient usersClient = new UserRestClient();
    private static final String defaultPassword = "12345";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = usersClient.createUser(username, defaultPassword);

                        usersClient.createIncomeInvitations(testUser, userAnno.incomeInvitations());
                        usersClient.createOutcomeInvitations(testUser, userAnno.outcomeInvitations());
                        usersClient.createFriends(testUser, userAnno.friends());
                        setUser(testUser);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getUserJson();
    }

    public static void setUser(UserJson testUser) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                testUser
        );
    }

    public static UserJson getUserJson() {
        final ExtensionContext context = TestMethodContextExtension.context();
        System.out.println(context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class));
        return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
    }
}