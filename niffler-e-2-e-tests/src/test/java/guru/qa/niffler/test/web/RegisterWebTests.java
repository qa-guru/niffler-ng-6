package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.BaseWebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class RegisterWebTests extends BaseWebTest {

    private static final Config CFG = Config.getInstance();
    private static final String SUCCESS_MESSAGE = "Congratulations! You've registered!";
    private static final String ALREADY_EXIST_ERROR = "Username `%s` already exists";
    private static final String PASSWORDS_NOT_EQUAL_ERROR = "Passwords should be equal";
    private static final String PASSWORD = "12345";
    private static final Faker faker = new Faker();
    private final String USERNAME = faker.funnyName().name();


    @Test
    public void shouldRegisterNewUser() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .clickSubmitButton()
                .checkSuccessMessage(SUCCESS_MESSAGE);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {

        String existUserErrorText = String.format(ALREADY_EXIST_ERROR, USERNAME);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .clickSubmitButton()
                .checkSuccessMessage(SUCCESS_MESSAGE)
                .clickSignInButton()
                .clickRegisterButton()
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .clickSubmitButton()
                .checkErrorTitle(existUserErrorText);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(faker.funnyName().name())
                .setPassword(PASSWORD)
                .setPasswordSubmit(faker.cat().name())
                .clickSubmitButton()
                .checkErrorTitle(PASSWORDS_NOT_EQUAL_ERROR);
    }
}

