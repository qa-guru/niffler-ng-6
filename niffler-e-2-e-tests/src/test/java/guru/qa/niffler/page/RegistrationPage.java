package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RegistrationPage {
    private final SelenideElement usernameInput = $("input[name='username']").as("инпут ввода имени");
    private final SelenideElement passwordInput = $$("[type='password']").get(0).as("инпут ввода пароля");
    private final SelenideElement confirmPasswordInput = $$("[type='password']").get(1).as("инпут повторения пароля");
    private final SelenideElement submitButton = $("button[type='submit']").as("кнопка 'Sign Up'");
    private final SelenideElement successRegistrationText = $(".form__paragraph_success")
            .as("сообщение об успешной регистрации");
    private final SelenideElement signInBtn = $(".form_sign-in").as("кнопки 'Sign In'");
    private final SelenideElement formErrorText = $(".form__error").as("ошибка регистрации");

    @Step("Ввести логин в инпут")
    public RegistrationPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Ввести пароль в инпут")
    public RegistrationPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Повторить пароль")
    public RegistrationPage setPasswordConfirm(String password) {
        confirmPasswordInput.setValue(password);
        return this;
    }

    @Step("Нажать на кнопку 'Sign Up'")
    public RegistrationPage clickSubmitButton() {
        submitButton.click();
        return this;
    }

    @Step("Появилось сообщение об успешной регистрации. Отображается кнопка авторизации'")
    public RegistrationPage registrationIsSuccessCheck() {
        successRegistrationText.shouldBe(visible).shouldHave(text("Congratulations! You've registered!"));
        signInBtn.shouldBe(visible);
        return this;
    }

    @Step("Появилось сообщение об ошибке регистрации")
    public RegistrationPage registrationErrorCheck(String text) {
        formErrorText.shouldBe(visible).shouldHave(text(text));
        return this;
    }

    @Step("Зарегистрироваться")
    public RegistrationPage registration(String username, String password, String confirmPassword) {
        setUsername(username);
        setPassword(password);
        setPasswordConfirm(confirmPassword);
        clickSubmitButton();
        return this;
    }
}
