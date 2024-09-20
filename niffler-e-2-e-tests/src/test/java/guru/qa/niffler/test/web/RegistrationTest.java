package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegistrationTest {
    RegisterPage registerPage = new RegisterPage();
    private static final Config CFG = Config.getInstance();
    final String REGISTERED_USER_NAME = "duck";
    final String EXPECTED_REGISTRATION_MESSAGE = "Congratulations! You've registered!";
    final String USERNAME_ALREADY_EXISTS_ERROR_TEXT = "Username `duck` already exists";
    final String PASSWORDS_NOT_EQUAL_ERROR_TEXT = "Passwords should be equal";
    String password = randomPassword();

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .submitCreateNewAccount();
        registerPage.setUsername(randomUsername())
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessRegisterNewUser(EXPECTED_REGISTRATION_MESSAGE);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .submitCreateNewAccount();
        registerPage.setUsername(REGISTERED_USER_NAME)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkFormErrorText(USERNAME_ALREADY_EXISTS_ERROR_TEXT);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .submitCreateNewAccount();
        registerPage.setUsername(randomUsername())
                .setPassword(randomPassword())
                .setPasswordSubmit(randomPassword())
                .submitRegistration()
                .checkFormErrorText(PASSWORDS_NOT_EQUAL_ERROR_TEXT);
    }
}