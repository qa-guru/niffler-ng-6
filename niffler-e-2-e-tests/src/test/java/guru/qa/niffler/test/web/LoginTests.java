package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginTests {

    private static final Config CFG = Config.getInstance();
    private static final String STATISTIC_HEADER = "Statistics";
    private static final String SPENDING_HEADER = "History of Spendings";

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String errorTitle = "Bad credentials";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("not_duck", "12345");

        new LoginPage().checkErrorTitle(errorTitle);
    }

    @Test
    public void mainPageShouldBeDisplayAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .checkSpendingHeader(SPENDING_HEADER)
                .checkStatisticsHeader(STATISTIC_HEADER);
    }
}
