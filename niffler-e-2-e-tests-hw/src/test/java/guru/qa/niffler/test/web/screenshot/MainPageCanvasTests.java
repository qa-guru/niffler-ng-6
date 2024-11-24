package guru.qa.niffler.test.web.screenshot;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.MainPage;
import guru.qa.niffler.page.page.ProfilePage;
import guru.qa.niffler.page.page.auth.LoginPage;
import guru.qa.niffler.utils.CategoryUtils;
import guru.qa.niffler.utils.SpendUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.model.rest.CurrencyValues.RUB;

@WebTest
class MainPageCanvasTests {

    private static final String EMPTY = "img/spend-stats/canvas-empty.png",
            ONE_SPEND = "img/spend-stats/canvas-1-spend.png",
            TWO_SPENDS_IN_ONE_CATEGORY = "img/spend-stats/canvas-2-spends-1-category.png",
            TWO_SPENDS_IN_TWO_CATEGORIES = "img/spend-stats/canvas-2-spends-2-categories.png";

    private static final CurrencyValues CURRENCY = RUB;
    private static final double AMOUNT_1 = 10.0, AMOUNT_2 = 50.0;

    @Test
    void shouldBeEmptyCanvasTest(@CreateNewUser(currency = RUB) UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(EMPTY);

    }

    @Test
    void shouldHaveOneSpendInCanvasTest(@CreateNewUser(currency = RUB) UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(
                        SpendUtils.generateForUser(user.getUsername())
                                .setCurrency(CURRENCY)
                                .setAmount(AMOUNT_1))
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(ONE_SPEND);
    }

    @Test
    void shouldHaveTwoSpendsWithOneCategoryInCanvasTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    currency = RUB
            )
            UserJson user
    ) {

        var category = CategoryUtils.generateForUser(user.getUsername());
        var spend1 = SpendUtils.generateForUser(user.getUsername())
                .setCategory(category)
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_1);
        var spend2 = SpendUtils.generateForUser(user.getUsername())
                .setCategory(category)
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_2);

        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(spend1)
                .createNewSpending(spend2)
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(TWO_SPENDS_IN_ONE_CATEGORY);
    }

    @Test
    @SneakyThrows
    void shouldHaveTwoSpendsInTwoCategoriesTest(@CreateNewUser(currency = RUB) UserJson user) {

        var spend1 = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_1);

        var spend2 = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_2);

        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(spend1)
                .createNewSpending(spend2)
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(TWO_SPENDS_IN_TWO_CATEGORIES);
    }

    @Test
    @SneakyThrows
    void shouldHaveOneSpendAfterRemoveTest(@CreateNewUser(currency = RUB) UserJson user) {

        var spend1 = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(CURRENCY)
                .setAmount(AMOUNT_1);

        var spend2 = SpendUtils.generateForUser(user.getUsername());

        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(spend1)
                .createNewSpending(spend2)
                .selectSpending(spend2.getDescription())
                .deleteSpendings()
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(ONE_SPEND);
    }

    @Test
    @SneakyThrows
    void shouldBeEmptyAfterRemoveTest(@CreateNewUser(currency = RUB) UserJson user) {
        var spend = SpendUtils.generateForUser(user.getUsername())
                .setCurrency(RUB);
        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(spend)
                .selectSpending(spend)
                .deleteSpendings()
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(EMPTY);
    }

    @Test
    @SneakyThrows
    void shouldBeEmptyAfterArchiveCategoryTest(@CreateNewUser(currency = RUB) UserJson user) {
        var spend = SpendUtils.generateForUser(user.getUsername()).setCurrency(CURRENCY).setAmount(AMOUNT_1);
        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(spend)
                .getHeader()
                .goToProfilePage()
                .setCategoryArchive(spend.getCategory().getName())
                .getHeader()
                .goToMainPage()
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(ONE_SPEND);
    }

    @Test
    @SneakyThrows
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
                            )
                    })
            UserJson user
    ) {
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .showArchivedCategories()
                .setCategoryActive(user.getTestData().getCategories().getFirst().getName())
                .getHeader()
                .goToMainPage()
                .shouldVisiblePageElements()
                .shouldHaveScreenshotSpendsStat(TWO_SPENDS_IN_ONE_CATEGORY);
    }


}
