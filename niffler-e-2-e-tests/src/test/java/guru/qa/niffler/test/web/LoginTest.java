package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.BaseTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;

@WebTest
public class LoginTest extends BaseTest {

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .mainPageAfterLoginCheck();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .auth("kisa", "kiss")
                .loginErrorCheck("Bad credentials");
    }
}
