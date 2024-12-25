package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
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
@Order(2)
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @User(

            categories = @Category(
                    archived = true
    )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345")
                .openProfilePage()
                .turnOnShowArchivedCategory()
                .checkArchivedCategoryIsDisplay(category.name());
    }

    @User(
            username = "esa",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345")
                .openProfilePage()
                .turnOnShowArchivedCategory()
                .checkArchivedCategoryIsDisplay(category.name());
    }

    @Test
    void changeName() {
        String name = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345")
                .openProfilePage()
                .changeName(name);
    }

    @User
   @ScreenShotTest("img/expected1.png")
    void checkCorrectUploadAvatar(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
        BufferedImage actualAvatar = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePage()
                .uploadAvatar("img/expected-stat.png")
                .avatarScreenshot();

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                actualAvatar
        ));
    }


}