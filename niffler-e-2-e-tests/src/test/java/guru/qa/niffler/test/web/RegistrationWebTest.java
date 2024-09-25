package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.faker.RandomDataUtils;
import guru.qa.niffler.jupiter.annotations.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.faker.RandomDataUtils.randomPassword;
import static guru.qa.niffler.faker.RandomDataUtils.randomUsername;
import static io.qameta.allure.Allure.step;

@DisplayName("Регистрация")
@WebTest
public class RegistrationWebTest extends BaseWebTest {

    private final String username = randomUsername();
    private final String validPassword = randomPassword(4, 10);
    private final String invalidPassword = randomPassword(1, 3);

    @Test
    @Story("Успешная регистрация нового пользователя")
    @DisplayName("Выполняем регистрацию нового пользователя и проверяем текст об успешной регистрации")
    public void testShouldRegisterNewUser() {

        final String successfulRegistrationText = "Congratulations! You've registered!";

        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .clickCreateNewAccount();
        });
        step("Заполнить форму регистрации валидными данными и кликнуть кнопку [Sing Up]", () -> {
            page.registerPage.setUsername(username)
                    .setPassword(validPassword)
                    .setConfirmPassword(validPassword)
                    .clickSignUpButton();
        });
        step("Отображается сообщение об успешной регистрации нового пользователя", () -> {
            page.registerPage.checkMessageThatRegistrationWasSuccessful(successfulRegistrationText);
        });
    }

    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Попытка регистрации с username ранее зарегистрированного пользователя")
    public void testShouldNotRegisterUserWithExistingUsername() {
        final String earlieRegisterUserName = "vladislav";

        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .clickCreateNewAccount();
        });
        step("Заполнить форму регистрации. В поле username ввести данные ранее зарегистрированного пользователя", () -> {
            page.registerPage.setUsername(earlieRegisterUserName)
                    .setPassword(validPassword)
                    .setConfirmPassword(validPassword)
                    .clickSignUpButton();
        });
        step("Отображается сообщение, что выбранный username уже существует", () -> {
            page.registerPage.checkMessageThatUsernameAlreadyExist(earlieRegisterUserName);
        });
    }

    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Попытка регистрации с разными значениями для поля password и passwordSubmit")
    public void testShouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        final String passwordsNotEqualsMessage = "Passwords should be equal";

        step("Открыть страницу регистрации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class)
                    .clickCreateNewAccount();
        });
        step("Заполнить форму регистрации. В поле passwordSubmit ввести значение, которое отличается от password", () -> {
            page.registerPage.setUsername(username)
                    .setPassword(validPassword)
                    .setConfirmPassword(RandomDataUtils.randomPassword(4, 10))
                    .clickSignUpButton();
        });
        step("Отображается сообщение, что пароли не совпадают", () -> {
            page.registerPage.checkMessageThatPasswordAndSubmitPasswordNotEquals(passwordsNotEqualsMessage);
        });
    }
}
