package guru.qa.niffler.page.nonstatic;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;

@Slf4j
@ParametersAreNonnullByDefault
public class NonStaticConfirmRegistrationPage extends NonStaticBasePage<NonStaticConfirmRegistrationPage> {

    private final SelenideElement successfulRegistrationLabel = driver.$(byText("Congratulations! You've registered!")).as("Congratulation text");
    private final SelenideElement signInButton = driver.$(byText("Sign in")).as("Sign in button");


    public NonStaticConfirmRegistrationPage() {
        super((SelenideDriver) WebDriverRunner.getWebDriver());
    }

    public NonStaticConfirmRegistrationPage(SelenideDriver driver) {
        super(driver);
    }

    @Override
    public NonStaticConfirmRegistrationPage shouldVisiblePageElement() {
        log.info("Assert confirm registration page element are visible");
        successfulRegistrationLabel.shouldBe(visible, Duration.ofSeconds(2));
        return this;
    }

    @Override
    @Step("Should visible 'Confirm Registration' page")
    public NonStaticConfirmRegistrationPage shouldVisiblePageElements() {
        log.info("Assert confirm registration page elements are visible");
        successfulRegistrationLabel.shouldBe(visible, Duration.ofSeconds(2));
        signInButton.shouldBe(visible);
        return this;
    }

}