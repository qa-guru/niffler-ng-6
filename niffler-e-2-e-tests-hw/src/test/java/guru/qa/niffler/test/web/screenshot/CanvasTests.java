package guru.qa.niffler.test.web.screenshot;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.page.auth.LoginPage;
import guru.qa.niffler.utils.ScreenDiffResult;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CanvasTests {

    private static final String LOGIN_PAGE_URL = Config.getInstance().frontUrl();

    @Test
    @SneakyThrows
    @ScreenshotTest("img/expected-stat-empty.jpeg")
    void shouldNotHaveSpends(@CreateNewUser UserJson user, BufferedImage expected) {
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword());


        BufferedImage actual = ImageIO.read($("canvas[role='img']").shouldBe(visible).screenshot());
        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));

    }

    @Test
    @ScreenshotTest("img/expected-stat-one-spend.jpeg")
    void shouldHaveOneSpend(@CreateNewUser UserJson user, BufferedImage expected) {

    }

    @Test
    @ScreenshotTest("img/expected-stat_two_spends_one_category.jpeg")
    void shouldHaveTwoSpendsInOneCategoryTest(@CreateNewUser UserJson user, BufferedImage expected) {

    }

    @Test
    @ScreenshotTest("img/expected-stat_two_spends_two_categories.jpeg")
    void shouldHaveTwoSpendsInTwoCategories(@CreateNewUser UserJson user, BufferedImage expected) {

    }

    @Test
    @ScreenshotTest("img/expected-stat-one-spend.jpeg")
    void shouldHaveOneSpendAfterRemove(@CreateNewUser UserJson user, BufferedImage expected) {

    }

    @Test
    @ScreenshotTest("img/expected-stat_empty.jpeg")
    void shouldBeEmptyAfterRemove(@CreateNewUser UserJson user, BufferedImage expected) {

    }

}
