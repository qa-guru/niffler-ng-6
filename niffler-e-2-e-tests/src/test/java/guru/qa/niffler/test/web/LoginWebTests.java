package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginWebTests {

    private static final Config CFG = Config.getInstance();
    private static final String BAD_CREDENTIALS = "Bad credentials";
    private final LoginPage loginPage = new LoginPage();

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("not_duck", "12345");

        loginPage.checkError(BAD_CREDENTIALS);
    }


    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin("duck", "12345")
                .checkThatPageLoaded();
    }
}
