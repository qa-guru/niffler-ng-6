package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.BaseTest;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginTest extends BaseTest {
    String userData = "kisa";

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(userData, userData)
                .mainPageAfterLoginCheck();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .auth(userData, "kiss")
                .loginErrorCheck("Bad credentials");
    }
}
