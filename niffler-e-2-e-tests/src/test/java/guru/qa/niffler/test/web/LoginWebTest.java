package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();


    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String errorTitle = "Неверные учетные данные пользователя";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123");

        new LoginPage().shouldErrorTitle(errorTitle);
    }
}

