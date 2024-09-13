package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement successRegisterMessage = $(".form__paragraph_success");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement errorTitle = $(".form__error");
    private final SelenideElement signInButton = $(".form_sign-in");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    public RegisterPage clickSubmitButton() {
        submitButton.click();
        return this;
    }

    public RegisterPage checkSuccessMessage(String successMessage) {
        successRegisterMessage.shouldHave(text(successMessage));
        return this;
    }

    public RegisterPage checkErrorTitle(String errorText) {
        errorTitle.shouldHave(text(errorText));
        return this;
    }

    public LoginPage clickSignInButton() {
        signInButton.click();
        return new LoginPage();
    }
}
