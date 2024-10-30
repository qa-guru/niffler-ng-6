package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.SignInPage;
import guru.qa.niffler.page.SignUpPage;
import guru.qa.niffler.page.SignUpSuccessfulPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WebTest
public class SignUpWebTest {

    private static final Config CFG = Config.getInstance();
    private String randomUsername;

    @BeforeEach
    void setUp() {
        randomUsername = RandomDataUtils.randomUsername();
    }

    @Test
    void shouldSignUpNewUser() {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .clickSignUpButton()
                .signUp(randomUsername, "12345");

        new SignUpSuccessfulPage()
                .checkThatRegistrationSuccessful()
                .clickSignInButton()
                .signIn(randomUsername, "12345")
                .checkSpendingModuleDisplayed();
    }

    @Test
    void shouldNotSignUpWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .clickSignUpButton()
                .signUp(randomUsername, "12345");

        new SignUpSuccessfulPage()
                .checkThatRegistrationSuccessful()
                .clickSignInButton()
                .clickSignUpButton()
                .signUp(randomUsername, "12345");

        new SignUpPage().shouldDisplayUserAlreadyExistsError(randomUsername);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .clickSignUpButton()
                .signUp(randomUsername, "12345", "123456");

        new SignUpPage().shouldDisplayPasswordMismatchError();
    }
}