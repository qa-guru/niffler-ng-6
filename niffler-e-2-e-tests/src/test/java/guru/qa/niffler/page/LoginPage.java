package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $("input[name='username']").as("инпут ввода имени");
    private final SelenideElement passwordInput = $("input[name='password']").as("инпут ввода пароля");
    private final SelenideElement submitButton = $("button[type='submit']").as("кнопка 'Log In'");
    private final SelenideElement registerButton = $(".form__register").as("кнопка 'Create new account'");
    private final SelenideElement formErrorText = $(".form__error").as("ошибка авторизации");

    @Step("Авторизоваться")
    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    @Step("Авторизоваться")
    public LoginPage auth(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return this;
    }

    @Step("Кликнуть по кнопке 'Create new account'")
    public RegistrationPage clickRegistrationBtn() {
        registerButton.click();
        return new RegistrationPage();
    }

    @Step("Появилось сообщение об ошибке авторизации")
    public LoginPage loginErrorCheck(String text) {
        formErrorText.shouldBe(visible).shouldHave(text(text));
        return this;
    }
}
