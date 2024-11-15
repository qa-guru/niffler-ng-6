package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static utils.FakerUtils.*;

@WebTest
@ExtendWith(BrowserExtension.class)
public class RegistrationWebTest {

    private static final Config CFG = Config.getInstance();
    RegisterPage registerPage = new RegisterPage();
    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();

    @Test
    void shouldRegisterNewUser() {
        String userName = USER_NAME;
        String password = PASSWORD;
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationPage();

        registerPage.successSignUp(userName, password);
        registerPage.checkSuccessSignUpAndClickSignIn();
        loginPage.login(userName, password);
        mainPage.checkingDisplayOfMainComponents();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String userName = USER_NAME;
        String password = PASSWORD;

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationPage();

        registerPage.successSignUp(userName, password);
        registerPage.checkSuccessSignUpAndClickSignIn();
        loginPage.openRegistrationPage();
        registerPage.successSignUp(userName, password);
        registerPage.usernameAlreadyExists(userName);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationPage();

        registerPage.signUpWithNotEqualPasswords(USER_NAME, PASSWORD, SECOND_PASSWORD);
        registerPage.checkPasswordsNotEqual();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(USER_NAME, PASSWORD);

        loginPage.checkBadCredentials();
        loginPage.checkHeaderLogin();
    }
}
