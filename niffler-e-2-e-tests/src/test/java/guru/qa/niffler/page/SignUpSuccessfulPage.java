package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SignUpSuccessfulPage {
    private final SelenideElement signInButton = $("a[class='form_sign-in']");
    private final SelenideElement successMessage = $(".form__paragraph.form__paragraph_success");

    public SignInPage clickSignInButton() {
        signInButton.click();
        return new SignInPage();
    }

    public SignUpSuccessfulPage checkThatRegistrationSuccessful() {
        successMessage.shouldHave(Condition.text("Congratulations! You've registered!"));
        return this;
    }
}