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
    private final SelenideElement successRegisterMessage = $(".form__paragraph_success");
    private final SelenideElement formError = $(".form__error");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    public RegisterPage setPasswordSubmit(String submitPassword) {
        submitPasswordInput.setValue(submitPassword);
        return new RegisterPage();
    }

    public RegisterPage submitRegistration() {
        submitButton.click();
        return new RegisterPage();
    }

    public void checkSuccessRegisterNewUser(String value) {
        successRegisterMessage.shouldHave(text(value)).shouldBe(visible);
    }

    public void checkFormErrorText(String value) {
        formError.shouldHave(text(value)).shouldBe(visible);
    }
}