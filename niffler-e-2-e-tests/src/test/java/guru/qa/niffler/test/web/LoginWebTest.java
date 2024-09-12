package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extensions.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.faker.FakeData.generateFakePassword;
import static io.qameta.allure.Allure.step;

@DisplayName("Авторизация")
@ExtendWith(BrowserExtension.class)
public class LoginWebTest extends BaseWebTest {

    private final String persistentName = "vladislav";
    private final String statisticsHeader = "Statistics";
    private final String historyOfSpendingHeader = "History of Spendings";
    private final String badCredentialMessage = "Неверные учетные данные пользователя";

    @Test
    @Story("Успешная авторизация")
    @DisplayName("Выполняем авторизацию пользователя")
    public void testMainPageShouldBeDisplayedAfterSuccessfulLogin() {

        String persistentPassword = "root";

        step("Открываем страницу авторизации и заполняем форму персистентными данными (username, password)", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .setUsername(persistentName)
                    .setPassword(persistentPassword)
                    .clickLogInButton();
        });
        step("Проверяем наличие заголовков на главное странице после авторизации", () -> {
            page.mainPage.checkThatNameOfStatisticsHeaderIsDisplayed(statisticsHeader)
                    .checkThatNameOfHistorySpendingHeaderIsDisplayed(historyOfSpendingHeader);
        });
    }

    @Test
    @Story("Неуспешная авторизация")
    @DisplayName("Выполняем авторизацию пользователя с невалидным паролем")
    public void testUserShouldStayOnLoginPageAfterLoginWithBadCredential() {

        String invalidPassword = generateFakePassword(1, 3);

        step("Открываем страницу авторизации и заполняем форму, используя персистентный username и случайный password", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .setUsername(persistentName)
                    .setPassword(invalidPassword)
                    .clickLogInButton();
        });
        step("Проверяем наличие сообщения о неверных учетных данных при авторизации", () -> {
            page.loginPage.checkMessageThatWasInputBadCredentials(badCredentialMessage);
        });
    }
}
