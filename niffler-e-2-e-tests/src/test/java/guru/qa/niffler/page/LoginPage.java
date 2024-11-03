package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $("a[href='/register']");

    @Step("Авторизуемся пользователем")
    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    @Step("Открываем страницу регистрации")
    public RegisterPage openRegisterPage() {
        registerButton.click();
        return new RegisterPage();
    }

    @Step("Проверяем, что кнопка войти отображается на странице")
    public void checkButtonSingInIsDisplayed() {
        submitButton.should(visible);
    }

    @Step("Вводим некорректные учетные данные")
    public LoginPage loginIncorrect(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new LoginPage();
    }
}