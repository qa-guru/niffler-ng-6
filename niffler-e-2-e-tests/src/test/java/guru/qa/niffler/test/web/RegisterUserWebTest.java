package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

@WebTest
@Order(2)
public class RegisterUserWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        final String userName = UUID.randomUUID().toString().substring(6);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .createUser(userName, "12345");
        new LoginPage().checkButtonSingInIsDisplayed();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        final String passordString = "12345";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .setUsername("esa")
                .setPassword(passordString)
                .setPasswordSubmit(passordString)
                .submitRegistration();
        new RegisterPage().checkMsgUserAlreadyExistIsDisplayed();
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        final String userName = UUID.randomUUID().toString().substring(6);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegisterPage()
                .setUsername(userName)
                .setPassword("12345")
                .setPasswordSubmit("54321")
                .submitRegistration();
        new RegisterPage().checkPasswordsShouldBeEqual();
    }
}