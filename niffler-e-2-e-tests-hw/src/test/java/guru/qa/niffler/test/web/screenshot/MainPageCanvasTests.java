package guru.qa.niffler.test.web.screenshot;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.ScreenshotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.auth.LoginPage;
import guru.qa.niffler.utils.CategoryUtils;
import guru.qa.niffler.utils.SpendUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static guru.qa.niffler.model.rest.CurrencyValues.RUB;

@WebTest
class MainPageCanvasTests {

    private static final String LOGIN_PAGE_URL = Config.getInstance().frontUrl();

    private static final CurrencyValues CURRENCY = RUB;
    private static final double AMOUNT_1 = 10.0, AMOUNT_2 = 50.0;

    @Test
    @ScreenshotTest(value = "img/spend-stats/canvas-empty.png")
    void shouldBeEmptyCanvasTest(@CreateNewUser(currency = RUB) UserJson user, BufferedImage expected) {
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);

    }

    @Test
    @ScreenshotTest(value = "img/spend-stats/canvas-1-spend.png")
    void shouldHaveOneSpendInCanvasTest(@CreateNewUser(currency = RUB) UserJson user, BufferedImage expected) {
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(
                        SpendUtils.generateForUser(user.getUsername())
                                .setCurrency(CURRENCY)
                                .setAmount(AMOUNT_1))
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);
    }

    @Test
    @ScreenshotTest(value = "img/spend-stats/canvas-2-spends-1-category.png")
    void shouldHaveTwoSpendsWithOneCategoryInCanvasTest(@CreateNewUser(currency = RUB) UserJson user, BufferedImage expected) {

        var category = CategoryUtils.generateForUser(user.getUsername());
        var spend1 = SpendUtils.generateForUser(user.getUsername())
                .setCategory(category)
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_1);
        var spend2 = SpendUtils.generateForUser(user.getUsername())
                .setCategory(category)
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_2);

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend1)
                .createNewSpending(spend2)
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);
    }

    @Test
    @SneakyThrows
    @ScreenshotTest(value = "img/spend-stats/canvas-2-spends-2-categories.png")
    void shouldHaveTwoSpendsInTwoCategoriesTest(@CreateNewUser(currency = RUB) UserJson user, BufferedImage expected) {

        var spend1 = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_1);

        var spend2 = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_2);

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend1)
                .createNewSpending(spend2)
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);
    }

    @Test
    @SneakyThrows
    @ScreenshotTest(value = "img/spend-stats/canvas-1-spend.png")
    void shouldHaveOneSpendAfterRemoveTest(@CreateNewUser(currency = RUB) UserJson user, BufferedImage expected) {

        var spend1 = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_1);

        var spend2 = SpendUtils.generateForUser(user.getUsername());

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend1)
                .createNewSpending(spend2)
                .selectSpending(spend2.getDescription())
                .deleteSpendings()
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);
    }

    @Test
    @SneakyThrows
    @ScreenshotTest(value = "img/spend-stats/canvas-empty.png")
    void shouldBeEmptyAfterRemoveTest(@CreateNewUser(currency = RUB) UserJson user, BufferedImage expected) {
        var spend = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(RUB);
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend)
                .selectSpending(spend)
                .deleteSpendings()
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);
    }

    @Test
    @SneakyThrows
    @ScreenshotTest(value = "img/spend-stats/canvas-1-spend.png")
    void shouldBeEmptyAfterArchiveCategoryTest(@CreateNewUser(currency = RUB) UserJson user, BufferedImage expected) {
        var spend = SpendUtils.generateForUser(user.getUsername()).setCurrency(CURRENCY).setAmount(AMOUNT_1);
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend)
                .getHeader()
                .goToProfilePage()
                .setCategoryArchive(spend.getCategory().getName())
                .getHeader()
                .goToMainPage()
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);
    }

    @Test
    @SneakyThrows
    @ScreenshotTest(value = "img/spend-stats/canvas-2-spends-1-category.png")
    void shouldHaveTwoSpendsAfterUnarchieveCategoryTest(
            @CreateNewUser(
                    currency = RUB,
                    categories = {
                            @Category(name = "travel", isArchived = true)
                    },
                    spendings = {
                            @Spending(
                                    category = "travel",
                                    currency = RUB,
                                    amount = 10
                            ),
                            @Spending(
                                    category = "travel",
                                    currency = RUB,
                                    amount = 50
                            ),
                    }
            ) UserJson user,
            BufferedImage expected) {

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToProfilePage()
                .showArchivedCategories()
                .setCategoryActive(user.getTestData().getCategories().getFirst().getName())
                .getHeader()
                .goToMainPage()
                .shouldVisiblePageElements()
                .shouldHaveSpendsStatCanvas(expected);
    }


}
