package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

@DisplayName("Авторизация")
public class LoginTest extends BaseWebTest {

    @Test
    @Story("Неуспешная авторизация")
    @DisplayName("Отображается страница авторизации после авторизации пользователя с неправильными 'Username' и 'Password'")
    void testLoginPageShouldBeDisplayedAfterLoginWithBagCredentials() {
        step("Открыть страницу авторизации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class);
        });
        step("Заполнить форму авторизации любыми 'Username' и 'Password' не зарегистрированными в системе", () -> {
            page.loginPage.login(faker.name().username(), faker.internet().password(3, 12));
        });
        step("Отображается ошибка о неверных данных пользователя, пользователь остается на странице авторизации", () ->
                page.loginPage.checkErrorAboutBadCredentialsIsDisplayed("Неверные учетные данные пользователя")
                        .checkUserStayOnLoginPageAfterLoginWithBadCredentials()
        );
    }
}
