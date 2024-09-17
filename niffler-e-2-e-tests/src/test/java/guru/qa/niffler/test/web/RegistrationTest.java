package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {
    RegisterPage registerPage = new RegisterPage();
    private static final Config CFG = Config.getInstance();
    final String REGISTERED_USER_NAME = "duck";
    final String EXPECTED_REGISTRATION_MESSAGE = "Congratulations! You've registered!";
    final String USERNAME_ALREADY_EXISTS_ERROR_TEXT = "Username `duck` already exists";
    final String PASSWORDS_NOT_EQUAL_ERROR_TEXT = "Passwords should be equal";
    Faker faker = new Faker();
    String password = faker.internet().password(3, 12);

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .submitCreateNewAccount();
        registerPage.setUsername(faker.name().username())
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
        registerPage.setUsername(faker.name().username())
                .setPassword(faker.internet().password(3, 12))
                .setPasswordSubmit(faker.internet().password(3, 12))
                .submitRegistration()
                .checkFormErrorText(PASSWORDS_NOT_EQUAL_ERROR_TEXT);
    }
}