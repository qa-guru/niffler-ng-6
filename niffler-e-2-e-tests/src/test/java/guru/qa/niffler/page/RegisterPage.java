package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitRegistrationBtn = $(".form__submit");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public void setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);
    }

    public void submitRegistration() {
        submitRegistrationBtn.click();
    }

    private final SelenideElement successRegisterMessage = $(".form__paragraph_success");

    public RegisterPage checkSuccessRegisterMessageIsDisplay() {
        successRegisterMessage.shouldBe(visible);
        return this;
    }

    private final SelenideElement signInBtn = $(".form_sign-in");

    public void checkSignInBtnIsDisplayed() {
        signInBtn.shouldBe(visible);
        signInBtn.shouldBe(clickable);
    }

    public LoginPage clickSignInBtn() {
        signInBtn.click();
        return new LoginPage();
    }

    private final ElementsCollection errorMessageIfPassNotEqualConfirmPass = $$(".form__error");

    public RegisterPage checkErrorMessageIfPassNotEqualConfirmPass(String errorMessage) {
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldBe(visible);
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldHave(text(errorMessage));
        return this;
    }

    public RegisterPage checkErrorMessageIfPassAndConfirmPassLessThenThreeSymbols(String errorMessage) {
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldBe(visible);
        errorMessageIfPassNotEqualConfirmPass.get(1).shouldBe(visible);
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldHave(text(errorMessage));
        errorMessageIfPassNotEqualConfirmPass.get(1).shouldHave(text(errorMessage));
        return this;
    }

    public RegisterPage checkErrorMessageIfUsernameLessThenThreeSymbols(String errorMessage) {
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldBe(visible);
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldHave(text(errorMessage));
        return this;
    }

    public RegisterPage checkErrorMessageIfUsernameExist(String errorMessage) {
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldBe(visible);
        errorMessageIfPassNotEqualConfirmPass.get(0).shouldHave(text(errorMessage));
        return this;
    }
}
