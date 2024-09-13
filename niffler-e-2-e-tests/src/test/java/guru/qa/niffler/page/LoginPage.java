package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            clickCreateNewAccountButton = $(".form__register"),
            error = $(".form__error");

    public void login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
    }

    public RegisterPage clickToRegisterPage() {
        clickCreateNewAccountButton.click();
        return new RegisterPage();
    }

    public void checkErrorBadCredentials() {
        error.shouldBe(visible).shouldHave(text("Bad credentials"));
    }
}
