package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            passwordSubmitInput = $("#passwordSubmit"),
            signUpButton = $(".form__submit"),
            registeredSuccessful = $(".form__paragraph_success"),
            registeredUnsuccessful = $(".form__error"),
            signInButton = $(".form_sign-in");

    public RegisterPage registeredUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        signUpButton.click();

        return new RegisterPage();
    }

    public LoginPage checkSuccessfulCreateUser() {
        registeredSuccessful.shouldHave(text("Congratulations! You've registered!"));
        signInButton.click();

        return new LoginPage();
    }

    public void checkUnsuccessfulCreateUser(String Username) {
        registeredUnsuccessful.shouldHave(text("Username `" + Username + "` already exists"));
    }

    public void checkLengthPasswordError() {
        registeredUnsuccessful.shouldHave(text("Allowed password length should be from 3 to 12 characters"));
    }
}
