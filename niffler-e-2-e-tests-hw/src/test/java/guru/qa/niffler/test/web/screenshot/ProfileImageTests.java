package guru.qa.niffler.test.web.screenshot;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.auth.LoginPage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

@WebTest
class ProfileImageTests {

    private static final String LOGIN_PAGE_URL = Config.getInstance().frontUrl();

    private final String img1 = "img/profile/original/img_1.png",
            img2 = "img/profile/original/img_2.png";

    @Test
    @ScreenshotTest(value = "img/profile/expected/img_1.png", rewriteScreenshot = true)
    void shouldHaveAvatarAfterUploadTest(
            @CreateNewUser UserJson user,
            BufferedImage expected
    ) {
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToProfilePage()
                .uploadAvatar(img1)
                .shouldVisiblePageElements()
                .shouldHaveAvatar(expected, 0.01);
    }

    @Test
    @ScreenshotTest(value = "img/profile/expected/img_2.png")
    void shouldHaveNewAvatarAfterUpdateTest(
            @CreateNewUser UserJson user,
            BufferedImage expected
    ) {
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToProfilePage()
                .uploadAvatar(img1)
                .shouldBeSuccessAlert()
                .uploadAvatar(img2)
                .shouldVisiblePageElements()
                .shouldHaveAvatar(expected, 0.01);
    }

}
