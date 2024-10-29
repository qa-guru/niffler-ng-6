package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement successRegisterMessage = $(".form__paragraph_success");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement signInButton = $(".form_sign-in");
    private final SelenideElement formError = $(".form__error");

    @Step("Заполнить имя пользователя: {username}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Заполнить пароль пользователя: {password}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Заполнить подтверждения пароль пользователя: {passwordSubmit}")
    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    @Step("Нажать на кнопку подтверждения")
    public RegisterPage clickSubmitButton() {
        submitButton.click();
        return this;
    }

    @Step("Нажать кнопку Войти")
    public LoginPage clickSignInButton() {
        signInButton.click();
        return new LoginPage();
    }

    @Step("Проверить успешное сообщение о регистрации: {value}")
    public RegisterPage shouldSuccessRegister(String value) {
        successRegisterMessage.shouldHave(text(value));
        return this;
    }

    @Step("Проверить ошибку регистрации: {value}")
    public RegisterPage shouldErrorRegister(String value) {
        formError.shouldHave(text(value));
        return this;
    }
}
