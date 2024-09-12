package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement createNewAccountButton = $(".form__register");
    private final SelenideElement formError = $(".form__error");

    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return new LoginPage();
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return new LoginPage();
    }

    public LoginPage submitButtonClick() {
        submitButton.click();
        return new LoginPage();
    }

    public RegisterPage submitCreateNewAccount() {
        createNewAccountButton.click();
        return new RegisterPage();
    }

    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    public void checkFormErrorText(String value) {
        formError.shouldHave(text(value)).shouldBe(visible);
    }
}