package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.BaseTest;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegistrationTest extends BaseTest {

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegistrationBtn()
                .registration(randomUsername(), randomPassword(), randomPassword())
                .registrationIsSuccessCheck();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String userName = "lissamissa";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegistrationBtn()
                .registration(userName, randomPassword(), randomPassword())
                .registrationErrorCheck("Username `" + userName + "` already exists");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegistrationBtn()
                .registration(randomUsername(), randomPassword(), randomPassword())
                .registrationErrorCheck("Passwords should be equal");
    }
}
