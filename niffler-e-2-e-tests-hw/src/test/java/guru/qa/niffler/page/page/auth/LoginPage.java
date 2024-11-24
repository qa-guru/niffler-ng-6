package guru.qa.niffler.page.page.auth;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.page.BasePage;
import guru.qa.niffler.page.page.MainPage;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = AUTH_URL + "login";

    private final SelenideElement title = $("h1").as("['Login Page' title]"),
            usernameInput = $(byName("username")).as("Username input"),
            passwordInput = $(byName("password")).as("Password input"),
            showPasswordButton = $("[class*='password-button']").as("Show password button"),
            submitButton = $("button[type='submit']").as("Submit button"),
            createNewAccountButton = $(byText("Create new account")).as("Create new account button"),
            badCredentialsError = $x("//p[@class='form__error' " +
                    "and (text()='Неверные учетные данные пользователя' or text()='Bad credentials')]") // INFO: Different language in browser and AT
                    .as("[Sign in form error]");

    public LoginPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    @Step("Sign in by username = [{}] and password = [{}]")
    public MainPage login(String username, String password) {
        log.info("Sign in for user [{}]", username);
        fillUsername(username);
        fillPassword(password);
        return submit();
    }

    @Step("Fill username = [{}]")
    private LoginPage fillUsername(String username) {
        log.info("Fill username = [{}]", username);
        usernameInput.setValue(username);
        return this;
    }

    @Step("Fill password = [{}]")
    private LoginPage fillPassword(String password) {
        log.info("Fill password = [{}]", password);
        passwordInput.setValue(password);
        return this;
    }

    @Step("Submit")
    public MainPage submit() {
        log.info("Submitting sign in");
        submitButton.shouldBe(clickable).click();
        return new MainPage();
    }

    @Step("Set show password = [{}]")
    public void showPassword(boolean status) {
        if (status != showPasswordButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password visible = [{}]", status);
            showPasswordButton.shouldBe(clickable).click();
        } else {
            log.info("Password visible status is already = [{}]", status);
        }
    }

    @Step("Click on 'Create new account'")
    public RegisterPage goToRegisterPage() {
        log.info("Go to 'RegisterPage'");
        createNewAccountButton.shouldBe(clickable).click();
        return new RegisterPage();
    }

    @Step("Should visible bad credentials error")
    public LoginPage shouldVisibleBadCredentialsError() {
        badCredentialsError.shouldBe(visible);
        return this;
    }

    @Override
    public LoginPage shouldVisiblePageElement() {
        log.info("Assert login page element visible");
        title.shouldBe(visible);
        title.shouldHave(text("Log in"));
        return this;
    }

    @Override
    @Step("Should visible 'Login' page")
    public LoginPage shouldVisiblePageElements() {

        log.info("Assert login page elements are visible");

        title.shouldBe(visible).shouldHave(exactText("Log in"));
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);

        return this;

    }

}