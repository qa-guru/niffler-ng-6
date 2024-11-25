package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.enums.CookieType;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.MainPage;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UserdataClient;
import guru.qa.niffler.service.api.impl.SpendApiClientImpl;
import guru.qa.niffler.service.api.impl.UserdataApiClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.Cookie;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Slf4j
@ParametersAreNonnullByDefault
public class ApiLoginExtension implements BeforeEachCallback, ParameterResolver {

    private static final Config CFG = Config.getInstance();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
    private final AuthApiClient authClient = new AuthApiClient();
    private static final UserdataClient userdataClient = new UserdataApiClientImpl();
    private static final SpendClient spendClient = new SpendApiClientImpl();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class) &&
                        parameter.isAnnotationPresent(ApiLogin.class))
                .forEach(parameter -> {

                    final String parameterName = parameter.getName();

                    ApiLogin anno = parameter.getAnnotation(ApiLogin.class);
                    UserJson createdUser = CreateNewUserExtension.getUserByTestParamName(parameterName);
                    String annoUsername = anno.username();
                    String createdUserUsername = createdUser != null
                            ? createdUser.getUsername()
                            : null;

                    UserJson userToLogin;

                    if (anno.username().isEmpty() || anno.password().isEmpty()) {

                        if (createdUser == null) {
                            throw new IllegalStateException("@%s must be present in case that @%s is empty!".formatted(
                                    CreateNewUser.class.getSimpleName(),
                                    ApiLogin.class.getSimpleName()
                            ));
                        }

                        userToLogin = createdUser;

                    } else {

                        UserJson fakeUser = UserJson.builder()
                                .username(anno.username())
                                .password(anno.password())
                                .build();

                        if (createdUser != null && !createdUserUsername.equals(annoUsername)) {
                            throw new IllegalStateException("@%1$s must not be present in case that @%2$s contains username or password." +
                                    "And @%1$s username [%3$s] not equals @%2$s username [%4$s]!."
                                            .formatted(
                                                    CreateNewUser.class.getSimpleName(),
                                                    ApiLogin.class.getSimpleName(),
                                                    createdUserUsername,
                                                    annoUsername
                                            ));
                        }

                        var user = userdataClient
                                .findByUsername(fakeUser.getUsername())
                                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + fakeUser.getUsername()));

                        TestData testData = user.getTestData()
                                .setCategories(spendClient.findAllCategoriesByUsername(user.getUsername()))
                                .setSpendings(spendClient.findAllByUsername(user.getUsername()))
                                .setIncomeInvitations(userdataClient.getIncomeInvitations(user.getUsername()))
                                .setOutcomeInvitations(userdataClient.getIncomeInvitations(user.getUsername()))
                                .setFriends(userdataClient.getIncomeInvitations(user.getUsername()));

                        user.setPassword(fakeUser.getPassword())
                                .setTestData(testData);

                        userToLogin = user;

                    }

                    var token = authClient.signIn(userToLogin.getUsername(), userToLogin.getPassword());
                    userToLogin
                            .getTestData()
                            .setToken(token)
                            .setCookies(ThreadSafeCookieStore.INSTANCE.getUserCookies(userToLogin.getUsername()));

                    CreateNewUserExtension.setUserByTestParamName(parameterName, userToLogin);

                });

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class) &&
                        parameter.isAnnotationPresent(ApiLogin.class) &&
                        parameter.getAnnotation(ApiLogin.class).setupBrowser())
                .findFirst()
                .ifPresent(parameter -> {

                    var user = CreateNewUserExtension.getUserByTestParamName(parameter.getName());
                    var token = user.getTestData().getToken();
                    Selenide.open(CFG.frontUrl());
                    Selenide.localStorage()
                            .setItem("id_token", token);
                    WebDriverRunner.getWebDriver().manage().addCookie(
                            new Cookie(
                                    CookieType.JSESSIONID.name(),
                                    CreateNewUserExtension.getUserByTestParamName(parameter.getName())
                                            .getTestData()
                                            .getCookieValueByName(CookieType.JSESSIONID.getCookieName())
                            )
                    );

                    Selenide.open(MainPage.URL, MainPage.class)
                            .shouldVisiblePageElement();
                });

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return parameter.getType().isAssignableFrom(UserJson.class) &&
                parameter.isAnnotationPresent(ApiLogin.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String paramName = parameterContext.getParameter().getName();
        return CreateNewUserExtension.getUserByTestParamName(paramName);
    }

}