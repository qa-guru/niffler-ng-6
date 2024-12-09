package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement usernameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement submitButton;
    private final SelenideElement registerButton;

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.usernameInput = driver.$("input[name='username']");
        this.passwordInput =  driver.$("input[name='password']");
        this.submitButton =  driver.$("button[type='submit']");
        this.registerButton =  driver.$("a[href='/register']");

    }

    public LoginPage() {
        this.usernameInput = $("input[name='username']");
        this.passwordInput =  $("input[name='password']");
        this.submitButton =  $("button[type='submit']");
        this.registerButton =  $("a[href='/register']");
    }


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
        return this;
    }
}