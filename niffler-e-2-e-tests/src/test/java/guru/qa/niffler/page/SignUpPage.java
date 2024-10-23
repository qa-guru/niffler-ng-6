package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

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
        signUp(username, password, true);
    }

    public void signUp(String username, String password, boolean confirmPasswordMatches) {
        setUsername(username);
        setPassword(password);
        if (confirmPasswordMatches) {
            setPasswordConfirmation(password);
        } else {
            setPasswordConfirmation(password + 6732542);
        }
        submitRegistration();
    }

    public boolean isUserAlreadyExistsErrorMessageDisplayed(String username) {
        String expectedError = "Username `" + username + "` already exists";
        return errorMessage.shouldHave(Condition.text(expectedError)).isDisplayed();
    }

    public boolean isPasswordMismatchErrorMessageDisplayed() {
        String expectedError = "Passwords should be equal";
        return errorMessage.shouldHave(Condition.text(expectedError)).isDisplayed();
    }
}