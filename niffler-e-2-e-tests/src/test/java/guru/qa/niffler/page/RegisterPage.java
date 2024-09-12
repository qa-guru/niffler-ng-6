package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement confirmPassword = $("input[name='passwordSubmit']");
    private final SelenideElement signUpButton = $("button[type=submit]");
    private final SelenideElement signInLink = $(".form_sign-in");
    private final SelenideElement successfulRegistrationMessage = $(".form__paragraph.form__paragraph_success");
    private final SelenideElement errorMessage = $("span.form__error");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setConfirmPassword(String password) {
        confirmPassword.setValue(password);
        return this;
    }

    public RegisterPage clickSignUpButton() {
        signUpButton.click();
        return this;
    }

    public LoginPage signIn() {
        signInLink.click();
        return new LoginPage();
    }

    public void checkMessageThatRegistrationWasSuccessful(String text) {
        successfulRegistrationMessage.shouldHave(text(text)).shouldBe(visible);
    }

    public void checkMessageThatUsernameAlreadyExist(String username) {
        String usernameAlreadyExistMessage = String.format("Username `%s` already exists", username);
        System.out.println(usernameAlreadyExistMessage);
        errorMessage.shouldHave(text(usernameAlreadyExistMessage)).shouldBe(visible);
    }

    public void checkMessageThatPasswordAndSubmitPasswordNotEquals(String text) {
        errorMessage.shouldHave(text(text)).shouldBe(visible);
    }
}
