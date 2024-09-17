package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitPasswordInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signInButton = $(".form_sign-in");
    private final SelenideElement successRegisterMessage = $(".form__paragraph_success");
    private final SelenideElement formError = $(".form__error");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        submitPasswordInput.setValue(password);
        return this;
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return this;
    }

    public LoginPage clickSignInButton() {
        signInButton.click();
        return new LoginPage();
    }

    public void successRegisterMessageShouldHaveText(String text) {
        successRegisterMessage.shouldHave(text(text)).shouldBe(visible);
    }

    public void formErrorShouldHaveText(String text) {
        formError.shouldHave(text(text)).shouldBe(visible);
    }
}
