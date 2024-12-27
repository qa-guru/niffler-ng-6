package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

@ExtendWith(BrowserExtension.class)
@WebTest
@Order(2)
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @ApiLogin
    @Test
    void archivedCategoryShouldPresentInCategoriesList(@Token String token, UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openProfilePage()
                .turnOnShowArchivedCategory()
                .checkArchivedCategoryIsDisplay(user.testData().categories().get(0).name());
    }

    @User(
            categories = @Category(
                    archived = false
            )
    )
    @ApiLogin
    @Test
    void activeCategoryShouldPresentInCategoriesList(@Token String token, UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openProfilePage()
                .turnOnShowArchivedCategory()
                .checkArchivedCategoryIsDisplay(user.testData().categories().get(0).name());
    }

    @ApiLogin(username = "esa", password = "12345")
    @Test
    void changeName(@Token String token) {
        String name = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openProfilePage()
                .changeName(name);
    }

    @User
    @ApiLogin
    @ScreenShotTest("img/expected1.png")
    void checkCorrectUploadAvatar(@Token String token, UserJson user, BufferedImage expected) throws IOException, InterruptedException {
        BufferedImage actualAvatar = Selenide.open(CFG.frontUrl(), MainPage.class)
                .openProfilePage()
                .uploadAvatar("img/expected-stat.png")
                .avatarScreenshot();

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                actualAvatar
        ));
    }


}