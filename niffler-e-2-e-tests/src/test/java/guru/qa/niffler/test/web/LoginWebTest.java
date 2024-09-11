package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.Test;

public class LoginWebTest {
    private static final Config CFG = Config.getInstance();
    private static final String statisticsText = "Statistics";
    private static final String historyOfSpendingText = "History of Spendings";
    private static final String failedLogInMessage = "Неверные учетные данные пользователя";
    MainPage mainPage = new MainPage();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        mainPage.checkStatisticsHeaderContainsText(statisticsText)
                .checkHistoryOfSpendingHeaderContainsText(historyOfSpendingText);
    }

    @Test
    void  userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername("duck")
                .setPassword("123")
                .submitButtonClick()
                .checkFormErrorText(failedLogInMessage);
    }
}