package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfirmRegistrationPage {

    private final SelenideElement successfulRegistrationLabel = $(byText("Congratulations! You've registered!"))
            .as("Congratulation text"),
            signInButton = $(byText("Sign in")).as("Sign in button");

    public LoginPage goToLoginPage() {
        signInButton.click();
        return new LoginPage();
    }

    public ConfirmRegistrationPage assertSuccessfulRegistration() {
        Assertions.assertAll("Assert successful login", () -> {
            assertTrue(successfulRegistrationLabel.is(visible, Duration.ofSeconds(2)));
            assertTrue(signInButton.isDisplayed());
        });
        return this;
    }

}
