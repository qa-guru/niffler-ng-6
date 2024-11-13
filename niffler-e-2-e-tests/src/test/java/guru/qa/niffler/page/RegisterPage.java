package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RegisterPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successRegistration = $x(".//p[text()=\"Congratulations! You've registered!\"]");
    private final SelenideElement signInButton = $x(".//a[text()='Sign in']");
    private final SelenideElement passwordsShouldBeEqual = $x(".//span[text()='Passwords should be equal']");

    @Step("Success registration")
    public void successSignUp(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        submitButton.click();
    }

    @Step("Check success sign up and click sign in")
    public void checkSuccessSignUpAndClickSignIn() {
        successRegistration.shouldBe(visible);
        signInButton.click();
    }

    @Step("Username already exists")
    public void usernameAlreadyExists(String username) {
        String usernameAlreadyExistsXpath = ".//span[text()='Username `%s` already exists']";
        $x(String.format(usernameAlreadyExistsXpath, username)).shouldBe(visible);
    }

    @Step("Sign up with not equal passwords")
    public void signUpWithNotEqualPasswords(String username, String password, String secondPassword) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(secondPassword);
        submitButton.click();
    }

    @Step("Check passwords not equal")
    public void checkPasswordsNotEqual() {
        passwordsShouldBeEqual.shouldBe(visible);
    }
}
