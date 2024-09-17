package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extantion.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345");
        Thread.sleep(1000);
        Assertions.assertTrue(new MainPage().checkStatisticBlockIsDisplayed());
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345");

        Assertions.assertFalse(new MainPage().checkStatisticBlockIsDisplayed());

    }

}
