package guru.qa.niffler.page.page.auth;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.page.BasePage;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class ConfirmRegistrationPage extends BasePage<ConfirmRegistrationPage> {

    private final SelenideElement successfulRegistrationLabel = $(byText("Congratulations! You've registered!"))
            .as("Congratulation text"),

    signInButton = $(byText("Sign in")).as("Sign in button");

    public ConfirmRegistrationPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    @Step("Click on link 'Sign in'")
    public LoginPage goToLoginPage() {
        log.info("Go to login page");
        signInButton.click();
        return new LoginPage();
    }

    @Override
    public ConfirmRegistrationPage shouldVisiblePageElement() {
        log.info("Assert confirm registration page element are visible");
        successfulRegistrationLabel.shouldBe(visible, Duration.ofSeconds(2));
        return this;
    }

    @Override
    @Step("Should visible 'Confirm Registration' page")
    public ConfirmRegistrationPage shouldVisiblePageElements() {
        log.info("Assert confirm registration page elements are visible");
        successfulRegistrationLabel.shouldBe(visible, Duration.ofSeconds(2));
        signInButton.shouldBe(visible);
        return this;
    }

}