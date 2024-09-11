package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement successRegisterMessage = $(".form__paragraph_success");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signInButton = $(".form_sign-in");
    private final SelenideElement formError = $(".form__error");

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

    public LoginPage clickSignInButton() {
        signInButton.click();
        return new LoginPage();
    }

    public RegisterPage shouldSuccessRegister(String value) {
        successRegisterMessage.shouldHave(text(value));
        return this;
    }

    public RegisterPage shouldErrorRegister(String value) {
        formError.shouldHave(text(value));
        return this;
    }


}
