package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.*;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ApiLoginExtension implements BeforeTestExecutionCallback, ParameterResolver {
    private static final Config CFG = Config.getInstance();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);

    private final AuthApiClient authApiClient = new AuthApiClient();
    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();
    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final UserdataApiClient userdataApiClient = new UserdataApiClient();

    private final boolean setupBrowser;

    private ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    public ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension rest() {
        return new ApiLoginExtension(false);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {

                    final UserJson userToLogin;
                    final UserJson userFromUserExtension = UserExtension.getUserJson();
                    if ("".equals(apiLogin.username()) || "".equals(apiLogin.password())) {
                        if (userFromUserExtension == null) {
                            throw new IllegalStateException("@User must be present in case that @ApiLogin is empty!");
                        }
                        userToLogin = userFromUserExtension;
                    } else {
                        List<CategoryJson> categories = categoriesApiClient.getCategories(apiLogin.username());
                        List<SpendJson> spends = spendApiClient.getSpends(apiLogin.username(), CurrencyValues.RUB, "2024-01-01", "2024-12-30");
                        List<UserJson> friends = userdataApiClient.getAllFriends(apiLogin.username())
                                .stream().filter(x -> FriendState.FRIEND.equals(x.friendState())).toList();
                        List<UserJson> outcomeInvitation = userdataApiClient.getAllPeople(apiLogin.username())
                                .stream().filter(x -> FriendState.INVITE_SENT.equals(x.friendState())).toList();
                        List<UserJson> incomeInvitation = userdataApiClient.getAllFriends(apiLogin.username())
                                .stream().filter(x -> FriendState.INVITE_RECEIVED.equals(x.friendState())).toList();
                        UserJson fakeUser = new UserJson(
                                apiLogin.username(),
                                new TestData(
                                        apiLogin.password(),
                                        categories,
                                        spends,
                                        friends,
                                        outcomeInvitation,
                                        incomeInvitation

                                )
                        );
                        if (userFromUserExtension != null) {
                            throw new IllegalStateException("@User must not be present in case that @ApiLogin contains username or password!");
                        }
                        UserExtension.setUser(fakeUser);
                        userToLogin = fakeUser;
                    }

                    final String token = authApiClient.getToken(
                            userToLogin.username(),
                            userToLogin.testData().password()
                    );
                    setToken(token);
                    if (setupBrowser) {
                        Selenide.open(CFG.frontUrl());
                        Selenide.localStorage().setItem("id_token", getToken());
                        WebDriverRunner.getWebDriver().manage().addCookie(
                                getJsessionIdCookie()
                        );
                        Selenide.open(MainPage.URL, MainPage.class).checkStatisticBlock();
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return "Bearer " + getToken();
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
    }

    public static Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
        );
    }
}
