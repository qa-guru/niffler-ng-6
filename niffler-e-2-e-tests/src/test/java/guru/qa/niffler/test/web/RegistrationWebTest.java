package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static utils.FakerUtils.*;

@WebTest
public class RegistrationWebTest extends BaseTest {

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
