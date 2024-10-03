package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.UserModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
public class RegisterPage {

    private final SelenideElement
            usernameInput = $("#username").as("Username input"),
            passwordInput = $("#password").as("Password input"),
            showPasswordButton = $("#passwordBtn").as("Show password button"),
            passwordConfirmationInput = $("#passwordSubmit").as("Submit button"),
            showPasswordConfirmationButton = $("#passwordSubmitBtn").as("Submit button"),
            submitButton = $("button[type='submit']").as("Sign up button"),
            goToLoginPageLink = $("[href$='/main']").as("Log in link");

    public ConfirmRegistrationPage signUp(@NonNull UserModel user) {
        fillRegistrationData(user);
        submit();
        return new ConfirmRegistrationPage();
    }

    public RegisterPage fillRegistrationData(@NonNull UserModel user) {

        log.info("Fill registration data: username = [{}], password = [{}], passwordConfirmation = [{}]",
                user.getUsername(), user.getPassword(), user.getPasswordConfirmation());

        usernameInput.setValue(user.getUsername());
        passwordInput.setValue(user.getPassword());
        passwordConfirmationInput.setValue(user.getPasswordConfirmation());

        return this;

    }

    public RegisterPage showPassword(boolean status) {
        if (status != showPasswordButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password text visible = [{}]", status);
            showPasswordButton.click();
        }
        return this;
    }

    public RegisterPage showPasswordConfirmation(boolean status) {
        if (status != showPasswordConfirmationButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password confirmation text visible = [{}]", status);
            showPasswordConfirmationButton.click();
        }
        return this;
    }

    private void submit() {
        log.info("Submit registration");
        submitButton.click();
    }

    public RegisterPage assertUsernameHasError(String error) {
        usernameInput.parent().$(".form__error").as("Username error text").shouldHave(text(error));
        return this;
    }

    public RegisterPage assertPasswordHasError(String error) {
        passwordInput.parent().$(".form__error").as("Password error text").shouldHave(text(error));
        return this;
    }

    public RegisterPage assertPasswordConfirmationHasError(String error) {
        passwordConfirmationInput.parent().$(".form__error").as("Password confirmation error text").shouldHave(text(error));
        return this;
    }

    public LoginPage goToLoginPage() {
        log.info("Go to login page");
        goToLoginPageLink.click();
        return new LoginPage();
    }

}
