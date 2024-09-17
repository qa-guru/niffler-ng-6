package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();
    String pass = randomPassword();

    @Test
    void shouldRegisterNewUser() {
        final String messageSuccessRegister = "Congratulations! You've registered!";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(randomUsername())
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldSuccessRegister(messageSuccessRegister);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String name = randomUsername();
        final String messageSuccessRegister = "Congratulations! You've registered!";
        final String messageErrorExistRegister = "Username `" + name + "` already exists";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(name)
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldSuccessRegister(messageSuccessRegister)
                .clickSignInButton()
                .clickRegisterButton()
                .setUsername(name)
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldErrorRegister(messageErrorExistRegister);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String name = randomUsername();
        final String messageErrorNotEqualPass = "Passwords should be equal";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(name)
                .setPassword(randomPassword())
                .setPasswordSubmit(randomPassword())
                .clickSubmitButton()

                .shouldErrorRegister(messageErrorNotEqualPass);
    }


}

