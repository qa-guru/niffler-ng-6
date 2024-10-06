package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.MainPage;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement title = $("h1").as("['Login Page' title]"),
            usernameInput = $(byName("username")).as("Username input"),
            passwordInput = $(byName("password")).as("Password input"),
            showPasswordButton = $("[class*='password-button']").as("Show password button"),
            submitButton = $("button[type='submit']").as("Submit button"),
            createNewAccountButton = $(byText("Create new account")).as("Create new account button"),
            badCredentialsError = $x("//p[@class='form__error' " +
                    "and (text()='Неверные учетные данные пользователя' or text()='Bad credentials')]") // INFO: Different language in browser and AT
                    .as("[Sign in form error]");

    public MainPage login(String username, String password) {
        log.info("Sign in for user [{}]", username);
        fillData(username, password);
        return submit();
    }

    public LoginPage fillData(String username, String password) {
        log.info("Fill authorization data: username = [{}], password = [{}]", username, password);
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        return this;
    }

    public MainPage submit() {
        log.info("Submitting sign in");
        submitButton.click();
        return new MainPage();
    }

    public void showPassword(boolean status) {
        if (status != showPasswordButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password visible = [{}]", status);
            showPasswordButton.click();
        } else {
            log.info("Password visible status is already = [{}]", status);
        }
    }

    public RegisterPage goToRegisterPage() {
        log.info("Go to 'RegisterPage'");
        createNewAccountButton.click();
        return new RegisterPage();
    }

    public LoginPage shouldVisibleBadCredentialsError() {
        badCredentialsError.shouldBe(visible);
        return this;
    }

    @Override
    public LoginPage shouldVisiblePageElements() {

        log.info("Assert login page elements are visible");

        title.shouldBe(visible).shouldHave(exactText("Log in"));
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);

        return this;

    }
}
