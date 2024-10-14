package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class LoginTest {
    private static final Config CFG = Config.getInstance();
    private static final String STATISTICS_TEXT = "Statistics";
    private static final String HISTORY_OF_SPENDING_TEXT = "History of Spendings";
    private static final String FAILED_LOGIN_MESSAGE = "Неверные учетные данные пользователя";
    MainPage mainPage = new MainPage();

    @User(
            categories = {
                    @Category(name = "cat_1", archived = false),
                    @Category(name = "cat_2", archived = true),
            },
            spendings = {
                    @Spending(
                            category = "cat_3",
                            description = "test_spend",
                            amount = 100
                    )
            }
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
        mainPage.checkStatisticsHeaderContainsText(STATISTICS_TEXT)
                .checkHistoryOfSpendingHeaderContainsText(HISTORY_OF_SPENDING_TEXT);
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(randomUsername())
                .setPassword("BAD")
                .submitButtonClick()
                .checkFormErrorText(FAILED_LOGIN_MESSAGE);
    }
}