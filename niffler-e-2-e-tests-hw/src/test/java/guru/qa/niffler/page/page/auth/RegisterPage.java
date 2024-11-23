package guru.qa.niffler.page.page.auth;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.BasePage;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

    public static final String URL = AUTH_URL + "register";

    private final SelenideElement
            title = $("h1").as("[Registration page title]"),
            usernameInput = $("#username").as("Username input"),
            passwordInput = $("#password").as("Password input"),
            showPasswordButton = $("#passwordBtn").as("Show password button"),
            passwordConfirmationInput = $("#passwordSubmit").as("Submit button"),
            showPasswordConfirmationButton = $("#passwordSubmitBtn").as("Submit button"),
            submitButton = $("button[type='submit']").as("Sign up button"),
            goToLoginPageLink = $("[href$='/main']").as("Log in link");

    public RegisterPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    @Step("Sign up user = [{user.username}]")
    public ConfirmRegistrationPage signUpSuccess(UserJson user) {
        log.info("Sign up user by: username = [{}], password = [{}], passwordConfirmation = [{}]",
                user.getUsername(), user.getPassword(), user.getPasswordConfirmation());
        fillUsername(user.getUsername());
        fillPassword(user.getPassword());
        fillPasswordConfirmation(user.getPasswordConfirmation());
        submit();
        return new ConfirmRegistrationPage();
    }

    @Step("Sign up user = [{user.username}]")
    public void signInFailed(UserJson user) {
        log.info("Sign up [FAILED] user by: username = [{}], password = [{}], passwordConfirmation = [{}]",
                user.getUsername(), user.getPassword(), user.getPasswordConfirmation());
        fillUsername(user.getUsername());
        fillPassword(user.getPassword());
        fillPasswordConfirmation(user.getPasswordConfirmation());
        submit();
    }

    @Step("Fill username = [{}]")
    public void fillUsername(String username) {
        usernameInput.setValue(username);
    }

    @Step("Fill password = [{}]")
    public void fillPassword(String password) {
        passwordInput.setValue(password);
    }

    @Step("Fill password confirmation = [{}]")
    public void fillPasswordConfirmation(String passwordConfirmation) {
        passwordConfirmationInput.setValue(passwordConfirmation);
    }

    public RegisterPage showPassword(boolean status) {
        if (status != showPasswordButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password text visible = [{}]", status);
            Allure.step("Set show password status = [" + status + "]", () ->
                    showPasswordButton.shouldBe(clickable).click()
            );
        }
        return this;
    }

    public RegisterPage showPasswordConfirmation(boolean status) {
        if (status != showPasswordConfirmationButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password confirmation text visible = [{}]", status);
            Allure.step("Set show password confirmation status = [" + status + "]", () ->
                    showPasswordConfirmationButton.shouldBe(clickable).click()
            );
        }
        return this;
    }

    @Step("Submit sign up")
    private void submit() {
        log.info("Submit registration");
        submitButton.shouldBe(clickable).click();
    }

    @Step("Should visible username error with text = [{}]")
    public RegisterPage assertUsernameHasError(String error) {
        usernameInput.parent().$(".form__error").as("Username error text").shouldHave(text(error));
        return this;
    }

    @Step("Should visible password error with text = [{}]")
    public RegisterPage assertPasswordHasError(String error) {
        passwordInput.parent().$(".form__error").as("Password error text").shouldHave(text(error));
        return this;
    }

    @Step("Should visible password confirmation error with text = [{}]")
    public RegisterPage assertPasswordConfirmationHasError(String error) {
        passwordConfirmationInput.parent().$(".form__error").as("Password confirmation error text").shouldHave(text(error));
        return this;
    }

    @Step("Click 'Log in!' link")
    public LoginPage goToLoginPage() {
        log.info("Go to login page");
        goToLoginPageLink.shouldBe(clickable).click();
        return new LoginPage();
    }

    @Override
    public RegisterPage shouldVisiblePageElement() {
        log.info("Assert registration page element is visible");
        title.shouldBe(visible);
        title.shouldHave(text("Sign up"));
        return null;
    }

    @Override
    @Step("Should visible 'Sign up' page")
    public RegisterPage shouldVisiblePageElements() {

        log.info("Assert registration page elements are visible");

        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordConfirmationInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        goToLoginPageLink.shouldBe(visible);

        return this;

    }

}