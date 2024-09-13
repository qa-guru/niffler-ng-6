package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;


public class RegisterTests {

    private static final Config CFG = Config.getInstance();
    private static final String SUCCESS_MESSAGE = "Congratulations! You've registered!";
    private static final String PASSWORD = "12345";
    Faker faker = new Faker();

    @Test
    public void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(faker.funnyName().name())
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .clickSubmitButton()
                .checkSuccessMessage(SUCCESS_MESSAGE);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String username = faker.funnyName().name();

        String existUserErrorText = String.format("Username `%s` already exists", username);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(username)
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .clickSubmitButton()
                .checkSuccessMessage(SUCCESS_MESSAGE)
                .clickSignInButton()
                .clickRegisterButton()
                .setUsername(username)
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .clickSubmitButton()
                .checkErrorTitle(existUserErrorText);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String passwordNotEqualsText = "Passwords should be equal";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(faker.funnyName().name())
                .setPassword(PASSWORD)
                .setPasswordSubmit(faker.cat().name())
                .clickSubmitButton()
                .checkErrorTitle(passwordNotEqualsText);
    }
}

