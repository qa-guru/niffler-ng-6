package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

@DisplayName("Регистрация")
public class RegistrationWebTest extends BaseWebTest {

    private final String validUserPassword= faker.internet().password(3, 12);
    private final String invalidUserPassword= faker.internet().password(1, 2);
    private final String username = faker.name().username();

    @Test
    @Story("Успешная регистрация")
    @DisplayName("Отображается сообщение об успешной регистрации после ввода валидных Username и Password")
    void shouldDisplaySuccessMessageAndSignInButtonAfterSuccessfulRegistration() {
        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .openRegisterPage();
        });
        step("Заполнить форму регистрации", () -> {
            page.registerPage.setUsername(faker.name().username())
                    .setPassword(validUserPassword)
                    .setPasswordSubmit(validUserPassword);
        });
        step("Отправить форму регистрации", () -> {
            page.registerPage.submitRegistration();
        });
        step("Проверить, что отображается сообщение об успешной регистрации и присутствует кнопка 'Sign in'", () -> {
            page.registerPage.checkSuccessRegisterMessageIsDisplay()
                    .checkSignInBtnIsDisplayed();
        });
    }

    @Test
    @Story("Успешная регистрация")
    @DisplayName("Отображается главная страница после успешной регистрации и последующей авторизации пользователя")
    void testMainPageShouldBeDisplayedAfterSuccessRegistrationAndLogin() {
        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .openRegisterPage();
        });
        step("Заполнить форму регистрации", () -> {
            page.registerPage.setUsername(username)
                    .setPassword(validUserPassword)
                    .setPasswordSubmit(validUserPassword);
        });
        step("Отправить форму регистрации", () -> {
            page.registerPage.submitRegistration();
        });
        step("Кликнуть на кнопку 'Sign in' и ввести login и password", () -> {
            page.registerPage.clickSignInBtn()
                    .login(username, validUserPassword);
        });
        step("Проверить, что таблица истории трат и статистика трат отображается", () -> {
            page.mainPage.isSpendingHistoryTableDisplayed()
                    .isSpendingStatisticsDisplayed();
        });
    }

    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Отображается сообщение об ошибке при несовпадении значений в полях 'Password' и 'Confirm password'")
    void testShouldErrorMessageIsDisplayedWhenPasswordAndConfirmPasswordAreNotEqual() {
        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .openRegisterPage();
        });
        step("Заполнить форму регистрации с разными значениями в полях 'Password' и 'Confirm password'", () -> {
            page.registerPage.setUsername(faker.name().username())
                    .setPassword(faker.internet().password(8, 12))
                    .setPasswordSubmit(faker.internet().password(8, 11));
        });
        step("Отправить форму регистрации", () -> {
            page.registerPage.submitRegistration();
        });
        step("Отображается сообщение что пароли не совпадают", () ->
                page.registerPage.checkErrorMessageIfPassNotEqualConfirmPass("Passwords should be equal")
        );
    }

    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Отображается сообщение об ошибке при вводе в поле 'Password' и 'Confirm password' менее 3 символов")
    void testShouldErrorMessageIsDisplayedWhenPasswordAndConfirmLessThenThreeSymbols() {
        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .openRegisterPage();
        });
        step("Заполнить форму регистрации с значениями в полях 'Password' и 'Confirm password' менее 3 символов", () -> {
            page.registerPage.setUsername(faker.name().username())
                    .setPassword(invalidUserPassword)
                    .setPasswordSubmit(invalidUserPassword);
        });
        step("Отправить форму регистрации", () -> {
            page.registerPage.submitRegistration();
        });
        step("Отображается сообщение что пароль слишком короткий", () ->
                page.registerPage.checkErrorMessageIfPassAndConfirmPassLessThenThreeSymbols("Allowed password length should be from 3 to 12 characters")
        );
    }

    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Отображается сообщение об ошибке при вводе в поле 'Username' менее 3 символов")
    void testShouldErrorMessageIsDisplayedWhenUsernameLessThenThreeSymbols() {
        String shortUsername = username.substring(1, Math.min(username.length(), 2));

        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .openRegisterPage();
        });
        step("Заполнить форму регистрации с значением в поле 'Username' менее трех символов", () -> {
            page.registerPage.setUsername(shortUsername)
                    .setPassword(validUserPassword)
                    .setPasswordSubmit(validUserPassword);
        });
        step("Отправить форму регистрации", () -> {
            page.registerPage.submitRegistration();
        });
        step("Отображается сообщение что пароли не совпадают", () ->
                page.registerPage.checkErrorMessageIfUsernameLessThenThreeSymbols("Allowed username length should be from 3 to 50 characters")
        );
    }

    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Отображается сообщение при попытке зарегистрировать существующего пользователя")
    void testShouldNotRegisterUserWithExistingUsername() {
        String username = "oleg";

        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .openRegisterPage();
        });
        step("Заполнить форму регистрации с значением в поле 'Username' ранее зарегистрированного пользователя", () -> {
            page.registerPage.setUsername(username)
                    .setPassword(validUserPassword)
                    .setPasswordSubmit(validUserPassword);
        });
        step("Отправить форму регистрации", () -> {
            page.registerPage.submitRegistration();
        });
        step("Отображается сообщение что пароли не совпадают", () ->
                page.registerPage.checkErrorMessageIfUsernameExist("Username `" + username + "` already exists")
        );
    }
}
