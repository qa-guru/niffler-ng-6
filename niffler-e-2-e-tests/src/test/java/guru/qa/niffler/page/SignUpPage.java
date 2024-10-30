package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class SignUpPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordConfirmationInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement errorMessage = $("span.form__error");

    public void setUsername(String username) {
        usernameInput.setValue(username);
    }

    public void setPassword(String password) {
        passwordInput.setValue(password);
    }

    public void setPasswordConfirmation(String password) {
        passwordConfirmationInput.setValue(password);
    }

    public void submitRegistration() {
        submitButton.click();
    }

    public void signUp(String username, String password) {
        signUp(username, password, password);
    }

    public void signUp(String username, String password, String passwordConfirmation) {
        setUsername(username);
        setPassword(password);
        setPasswordConfirmation(passwordConfirmation);
        submitRegistration();
    }

    public void shouldDisplayUserAlreadyExistsError(String username) {
        String expectedError = "Username `" + username + "` already exists";
        errorMessage.shouldHave(text(expectedError)).shouldBe(visible);
    }

    public void shouldDisplayPasswordMismatchError() {
        String expectedError = "Passwords should be equal";
        errorMessage.shouldHave(text(expectedError)).shouldBe(visible);
    }
}