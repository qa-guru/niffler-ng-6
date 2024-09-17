package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Setter;

import static com.codeborne.selenide.Selenide.$;


public class RegisterPage {

    private final SelenideElement userNameField = $("#username");
    private final SelenideElement passwordField = $("#password");
    private final SelenideElement passwordSubmitField = $("#passwordSubmit");
    private final SelenideElement signUpButton = $(".form__submit");
    private final SelenideElement passwordBtn = $("#passwordBtn");
    private final SelenideElement passwordSubmitBtn = $("#passwordSubmitBtn");
    private final SelenideElement successMessage = $(".form__paragraph_success");
    private final SelenideElement signInButton = $(".form_sign-in");

    public RegisterPage setUserName(String user) {
        userNameField.setValue(user);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordField.setValue(password);
        return this;
    }

    public RegisterPage setSubmitPassword(String submitPassword) {
        passwordSubmitField.setValue(submitPassword);
        return this;
    }

    public LoginPage clickSignUpBtn() {
        signUpButton.click();
        return new LoginPage();
    }

    public RegisterPage checkAllFields(String userName, String password, String submitPassword){
        passwordSubmitBtn.click();
        passwordBtn.click();
        userNameField.shouldHave(Condition.value(userName));
        passwordField.shouldHave(Condition.value(password));
        passwordSubmitField.shouldHave(Condition.value(submitPassword));
        return new RegisterPage();
    }

    public RegisterPage doRegister(String userName, String password, String submitPassword){
        setUserName(userName);
        setPassword(password);
        setSubmitPassword(submitPassword);

        checkAllFields(userName, password, submitPassword);
        clickSignUpBtn();
        return this;
    }

    public RegisterPage checkSuccessMessage(){
        successMessage.shouldHave(Condition.exactText("Congratulations! You've registered!"));
        return this;
    }

    public LoginPage clickSignInButton(){
        signInButton.click();
        return new LoginPage();
    }

    public RegisterPage checkErrorMessage( String errorMessage){
        $(".form__error").shouldHave(Condition.text(errorMessage));
        return this;
    }
}
