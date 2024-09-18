package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement proceedLoginButton = $(".form_sign-in");
    private final SelenideElement errorContainer = $(".form__error");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
        usernameInput.setValue(login);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        return this;
    }

    public LoginPage successSubmit() {
        submit();
        proceedLoginButton.click();
        return new LoginPage();
    }

    public void submit() {
        submitButton.click();
    }

    public RegisterPage checkAlertMessage(String errorMessage) {
        errorContainer.shouldHave(text(errorMessage));
        return this;
    }
}