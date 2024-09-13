package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

@ExtendWith(BrowserExtension.class)
public class RegisterUserWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser(){
        final String userName = UUID.randomUUID().toString().substring(6);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .createUser(userName, "12345");
        Assertions.assertTrue(new LoginPage().checkButtonSingInIsDisplayed());
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername(){
        final  String passordString = "12345";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .setUsername("esa")
                .setPassword(passordString)
                .setPasswordSubmit(passordString)
                .submitRegistration();

        Assertions.assertTrue(new RegisterPage().checkMsgUserAlreadyExistIsDisplayed());
    }
    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        final String userName = UUID.randomUUID().toString().substring(6);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .setUsername(userName)
                .setPassword("12345")
                .setPasswordSubmit("54321")
                .submitRegistration();

        Assertions.assertTrue(new RegisterPage().checkPasswordsShouldBeEqual());
    }

}
