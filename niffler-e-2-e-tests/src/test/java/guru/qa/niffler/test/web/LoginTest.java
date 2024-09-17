package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

public class LoginTest {
    private static final Config CFG = Config.getInstance();
    private static final String STATISTICS_TEXT = "Statistics";
    private static final String HISTORY_OF_SPENDING_TEXT = "History of Spendings";
    private static final String FAILED_LOGIN_MESSAGE = "Неверные учетные данные пользователя";
    MainPage mainPage = new MainPage();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        mainPage.checkStatisticsHeaderContainsText(STATISTICS_TEXT)
                .checkHistoryOfSpendingHeaderContainsText(HISTORY_OF_SPENDING_TEXT);
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername("duck")
                .setPassword("123")
                .submitButtonClick()
                .checkFormErrorText(FAILED_LOGIN_MESSAGE);
    }
}