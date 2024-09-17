package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
public class MainWebTest {

    private static final Config CFG = Config.getInstance();


    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        final String statisticsHeader = "Statistics";
        final String historyHeader = "History of Spendings";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123456");

        new MainPage().shouldStatisticsHeader(statisticsHeader)
                .shouldHistoryHeader(historyHeader);
    }
}

