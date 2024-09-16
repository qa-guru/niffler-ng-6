package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String errorTitle = "Неверные учетные данные пользователя";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123");

        new LoginPage().shouldErrorTitle(errorTitle);
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        final String statisticsHeader = "Statistics";
        final String historyHeader = "History of Spendings";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123456");

        mainPage.shouldStatisticsHeader(statisticsHeader)
                .shouldHistoryHeader(historyHeader);
    }
}

