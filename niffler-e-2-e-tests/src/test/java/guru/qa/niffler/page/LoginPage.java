package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $(".form__register");
    private final SelenideElement errorTitle = $(".form__error");
    private final SelenideElement errorContainer = $(".form__error");

    @Step("Авторизация под пользователем {username}")
    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    @Step("Успешная авторизация под пользователем {username}")
    public MainPage successLogin(String username, String password) {
        login(username, password);
        return new MainPage();
    }

    @Step("Нажать кнопку Регистрация")
    public RegisterPage clickRegisterButton() {
        registerButton.click();
        return new RegisterPage();
    }

    @Step("Проверить текст ошибки {value}")
    public LoginPage shouldErrorTitle(String value) {
        errorTitle.shouldHave(text(value));
        return this;
    }

    @Step("Проверить текст ошибки {value}")
    public LoginPage checkError(String error) {
        errorContainer.shouldHave(text(error));
        return this;
    }
}
