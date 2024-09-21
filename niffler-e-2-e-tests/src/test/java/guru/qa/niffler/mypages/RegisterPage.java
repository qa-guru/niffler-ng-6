package guru.qa.niffler.mypages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successMessage = $("p[class*='success']");
    private final SelenideElement errorRegisterMessage = $("span[class*='error']");
    private final SelenideElement signInAfterRegistrationButton = $("a[class*='sign-in']");

    public void setUserName(String userName) {
        usernameInput.shouldBe(visible)
                .setValue(userName);
    }

    public void setUserPassword(String password) {
        passwordInput.shouldBe(visible)
                .setValue(password);
    }

    public void setUserPasswordSubmit(String password) {
        passwordSubmitInput.shouldBe(visible)
                .setValue(password);
    }

    public LoginPage submitRegistration() {
        submitButton.shouldBe(visible)
                .click();
        errorRegisterMessage.shouldNot(visible);
        successMessage.shouldBe(visible);
        signInAfterRegistrationButton.shouldBe(visible)
                .click();
        return new LoginPage();
    }

    public void clickRegistration() {
        submitButton.shouldBe(visible)
                .click();
    }

    public RegisterPage checkRegisterPage() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordSubmitInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    public void checkErrorMessageContainsExpectedMessage(String errorMessage) {
        errorRegisterMessage.shouldHave(visible)
                .shouldBe(partialText(errorMessage));
    }
}
