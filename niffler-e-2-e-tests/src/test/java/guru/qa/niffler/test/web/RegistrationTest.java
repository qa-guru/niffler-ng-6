package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();
    private static final String USERNAME = RandomUtils.randomUserName();
    private static final String PASSWORD = "12345";

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doRegister()
                .fillRegisterPage(USERNAME, PASSWORD, PASSWORD)
                .successSubmit()
                .successLogin(USERNAME, PASSWORD)
                .checkThatPageLoaded();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String existingUsername = "duck";

        LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
        loginPage.doRegister()
                .fillRegisterPage(existingUsername, PASSWORD, PASSWORD)
                .submit();
        loginPage.checkError("Username `" + existingUsername + "` already exists");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {


        LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
        loginPage.doRegister()
                .fillRegisterPage(USERNAME, PASSWORD, "bad password submit")
                .submit();
        loginPage.checkError("Passwords should be equal");
    }
}
