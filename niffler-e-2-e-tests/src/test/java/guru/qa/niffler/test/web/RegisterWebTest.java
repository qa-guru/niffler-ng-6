package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.generator.DataGenerator.genPassword;
import static guru.qa.niffler.generator.DataGenerator.genUsername;

@ExtendWith(BrowserExtension.class)
public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();
    final String EXPECTED_REGISTRATION_MESSAGE = "Congratulations! You've registered!";
    final String USERNAME_ALREADY_EXISTS_ERROR_TEXT = "Username `duck` already exists";
    final String PASSWORDS_NOT_EQUAL_ERROR_TEXT = "Passwords should be equal";

    @Test
    void shouldRegisterNewUser() {
        String username = genUsername();
        String password = genPassword(3, 12);

        RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount();

        registerPage.setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .successRegisterMessageShouldHaveText(EXPECTED_REGISTRATION_MESSAGE);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String password = genPassword(3, 12);

        RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount();

        registerPage.setUsername("duck")
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .formErrorShouldHaveText(USERNAME_ALREADY_EXISTS_ERROR_TEXT);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        RegisterPage registerPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount();

        registerPage.setUsername(genUsername())
                .setPassword(genPassword(3, 12))
                .setPasswordSubmit(genPassword(3, 12))
                .submitRegistration()
                .formErrorShouldHaveText(PASSWORDS_NOT_EQUAL_ERROR_TEXT);
    }
}
