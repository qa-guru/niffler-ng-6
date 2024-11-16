package guru.qa.niffler.jupiter.extantion;


import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UserClient;

import guru.qa.niffler.service.db.UserDbClient;
import guru.qa.niffler.service.rest.UserRestClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;


public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

    private final UserClient userDbClient = new UserRestClient();
    private static final String defaultPassword = "12345";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if ("".equals(anno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = userDbClient.createUser(username, defaultPassword);
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                testUser.addTestData(
                                        new TestData(
                                                defaultPassword,
                                                new ArrayList<>(),
                                                new ArrayList<>()
                                        )
                                )
                        );
                    }

                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}