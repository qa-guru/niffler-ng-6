package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginWebTests {

    private static final Config CFG = Config.getInstance();
    private static final String STATISTIC_HEADER = "Statistics";
    private static final String SPENDING_HEADER = "History of Spendings";
    private static final String BAD_CREDENTIALS = "Bad credentials";

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("not_duck", "12345");

        new LoginPage().checkErrorTitle(BAD_CREDENTIALS);
    }

    @Test
    public void mainPageShouldBeDisplayAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .checkSpendingHeader(SPENDING_HEADER)
                .checkStatisticsHeader(STATISTIC_HEADER);
    }
}