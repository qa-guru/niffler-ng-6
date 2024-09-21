package guru.qa.niffler.mypages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement nifflerLogo = $("p[class*='logo']");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerNewUserBtn = $("a[class='form__register']");
    private final SelenideElement errorMessage = $("p[class*='error'");

    public void login(String username, String password) {
        usernameInput.shouldBe(visible)
                .setValue(username);
        passwordInput.shouldBe(visible)
                .setValue(password);
        submitButton.shouldBe(visible)
                .click();
    }

    public LoginPage checkLoginPage() {
        nifflerLogo.shouldBe(visible);
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        registerNewUserBtn.shouldBe(visible);
        return this;
    }

    public RegisterPage gotoRegisterNewUser() {
        registerNewUserBtn.shouldBe(visible)
                .click();
        return new RegisterPage();
    }

    public LoginPage checkErrorText(String errorText) {
        errorMessage.shouldBe(visible)
                .shouldBe(partialText(errorText));
        return this;
    }
}
