package guru.qa.niffler.test.web.screenshot;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.auth.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
class ProfileImageTests {

    private static final String LOGIN_PAGE_URL = Config.getInstance().frontUrl();

    private static final String ORIGINAL_IMG_1 = "img/profile/original/img_1.png",
            ORIGINAL_IMG_2 = "img/profile/original/img_2.png",
            EXPECTED_IMG_1 = "img/profile/expected/img_1.png",
            EXPECTED_IMG_2 = "img/profile/expected/img_2.png";

    @Test
    void shouldHaveAvatarAfterUploadTest(
            @CreateNewUser UserJson user
    ) {
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToProfilePage()
                .uploadAvatar(ORIGINAL_IMG_1)
                .shouldVisiblePageElements()
                .shouldHaveScreenshotAvatar(EXPECTED_IMG_1);
    }

    @Test
    void shouldHaveNewAvatarAfterUpdateTest(
            @CreateNewUser UserJson user
    ) {
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToProfilePage()
                .uploadAvatar(ORIGINAL_IMG_1)
                .shouldBeSuccessAlert()
                .uploadAvatar(ORIGINAL_IMG_2)
                .shouldVisiblePageElements()
                .shouldHaveScreenshotAvatar(EXPECTED_IMG_2);
    }

}
