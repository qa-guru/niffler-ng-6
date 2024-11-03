package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;


public class RegisterPage extends BasePage<RegisterPage> {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement singInButton = $("a[class='form_sign-in']");
    private final SelenideElement msgUsernameAlreadyExist = $(withText("already exists"));
    private final SelenideElement msgPasswordsShouldBeEqual = $(withText("Passwords should be equal"));


    @Step("Водим имя пользователя")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    @Step("Водим пароль")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    @Step("Водим проверочный пароль")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return new RegisterPage();
    }

    @Step("Подтверждаем регистрацию нового пользователя")
    public LoginPage submitRegistration() {
        submitButton.click();
        return new LoginPage();
    }

    @Step("Регистрируем нового пользователя в системе")
    public LoginPage createUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        submitButton.click();
        singInButton.click();
        return new LoginPage();
    }

    @Step("Проверяем отображение сообщения о, том что пользователь уже зарегистрирован в системе")
    public void checkMsgUserAlreadyExistIsDisplayed() {
        msgUsernameAlreadyExist.should(visible);
    }

    @Step("Проверяем, что пароли совпадают ")
    public void checkPasswordsShouldBeEqual() {
        msgPasswordsShouldBeEqual.should(visible);
    }
}