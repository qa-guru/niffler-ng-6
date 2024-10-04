package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ConfirmRegistrationPage {

    private final SelenideElement successfulRegistrationLabel = $(byText("Congratulations! You've registered!"))
            .as("Congratulation text"),
            signInButton = $(byText("Sign in")).as("Sign in button");

    public LoginPage goToLoginPage() {
        signInButton.click();
        return new LoginPage();
    }

    public ConfirmRegistrationPage assertSuccessfulRegistration() {
            successfulRegistrationLabel.shouldBe(visible, Duration.ofSeconds(2));
            signInButton.shouldBe(visible);
        return this;
    }

}
