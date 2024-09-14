package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(BrowserExtension.class)
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();


    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String errorTitle = "Bad credentials";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("ola", "1234xx");

        new LoginPage().shouldErrorTitle(errorTitle);
    }
}
