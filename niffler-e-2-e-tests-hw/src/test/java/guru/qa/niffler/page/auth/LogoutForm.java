package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
@NoArgsConstructor
public class LogoutForm extends BasePage<LogoutForm> {

    public LogoutForm(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    private final SelenideElement root = $x("//*[./h2[text()='Want to logout?']]").as("['Logout form' container]"),
            title = root.$("h2").as("['Logout' title]"),
            message = root.$("p").as("['Message' text]"),
            closeButton = root.$x(".//*[text()='Close']").as("['Close' button]"),
            logoutButton = root.$x(".//*[text()='Log out']").as("['Log out' button]");

    @Step("Submit log out")
    public LoginPage logout() {
        log.info("Log out");
        logoutButton.click();
        return new LoginPage();
    }

    @Step("Close logout form")
    public void close() {
        log.info("Close logout form");
        closeButton.click();
    }

    @Override
    public LogoutForm shouldVisiblePageElement() {
        log.info("Assert logout elements are visible");
        title.shouldBe(visible);
        title.shouldHave(text("Want to logout?"));
        return this;
    }

    @Override
    @Step("Should visible logout form")
    public LogoutForm shouldVisiblePageElements() {

        log.info("Assert logout elements are visible");

        title.shouldBe(visible);
        message.shouldBe(visible);
        closeButton.shouldBe(visible);
        logoutButton.shouldBe(visible);

        return this;

    }



}