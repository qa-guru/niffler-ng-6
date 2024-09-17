package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

@ExtendWith(BrowserExtension.class)
public class RegistrationWebTest {

    private static final Config CFG = Config.getInstance();
    private RegisterPage registerPage = new RegisterPage();
    private Faker faker = new Faker();
    private String password = faker.internet().password(3, 12);

    @Test
    void shouldRegisterNewUser() {
        String userName = faker.name().firstName() + new Random().nextInt(100);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickSignUpButton()
                .doRegister(userName, password, password)
                .checkSuccessMessage();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername(){
        String userName = faker.name().firstName() + new Random().nextInt(100);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickSignUpButton()
                .doRegister(userName, password, password)
                .checkSuccessMessage()
                .clickSignInButton()
                .clickSignUpButton()
                .doRegister(userName, password, password)
                .checkErrorMessage(String.format("Username `%s` already exists", userName));
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        String userName = faker.name().firstName() + new Random().nextInt(100);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickSignUpButton()
                .doRegister(userName, password, password.substring(0, 3) + "new")
                .checkErrorMessage("Passwords should be equal");
    }
}

