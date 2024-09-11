package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

public class RegisterWebTest {
    RegisterPage registerPage = new RegisterPage();
    private static final Config CFG = Config.getInstance();
    private static final String REGISTERED_USER_NAME = "duck";
    Faker faker = new Faker();
    String password = faker.internet().password(3, 12);
    final String expected = "Congratulations! You've registered!";
    final String alreadyExistsUserNameErrorText = "Username `duck` already exists";
    final String errorIfPasswordAndConfirmPasswordAreNotEqual = "Passwords should be equal";

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount();
        registerPage.setUsername(faker.name().username())
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessRegisterNewUser(expected);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount();
        registerPage.setUsername(REGISTERED_USER_NAME)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkFormErrorText(alreadyExistsUserNameErrorText);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount();
        registerPage.setUsername(faker.name().username())
                .setPassword(faker.internet().password(3, 12))
                .setPasswordSubmit(faker.internet().password(3, 12))
                .submitRegistration()
                .checkFormErrorText(errorIfPasswordAndConfirmPasswordAreNotEqual);
    }
}