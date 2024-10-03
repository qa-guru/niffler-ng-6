package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
public class LogoutForm {

    public LogoutForm assertPage() {
        log.info("Assert logout form elements");
        title.shouldBe(visible);
        message.shouldBe(visible);
        closeButton.shouldBe(visible);
        logoutButton.shouldBe(visible);
        return this;
    }    private final SelenideElement root = $x("//*[./h2[text()='Want to logout?']]").as("['Logout form' container]"),
            title = root.$("h2").as("['Logout' title]"),
            message = root.$("p").as("['Message' text]"),
            closeButton = root.$x(".//*[text()='Close']").as("['Close' button]"),
            logoutButton = root.$x(".//*[text()='Log out']").as("['Log out' button]");

    public void close() {
        log.info("Close logout form");
        closeButton.click();
    }

    public LoginPage logout() {
        log.info("Log out");
        logoutButton.click();
        return new LoginPage();
    }



}
