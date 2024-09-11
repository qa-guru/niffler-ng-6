package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegisterWebTest {
    Faker faker = new Faker();
    private static final Config CFG = Config.getInstance();
    String pass = faker.internet().password(3, 15);

    @Test
    void shouldRegisterNewUser() {
        final String messageSuccessRegister = "Congratulations! You've registered!";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(faker.name().username())
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldSuccessRegister(messageSuccessRegister);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String name = faker.name().username();
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
        String name = faker.name().username();
        final String messageErrorNotEqualPass = "Passwords should be equal";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(name)
                .setPassword(faker.internet().password(3, 15))
                .setPasswordSubmit(pass)
                .clickSubmitButton()

                .shouldErrorRegister(messageErrorNotEqualPass);
    }


}

