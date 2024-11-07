package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@WebTest
public class SpendingWebTest {
    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getSpendingTable()
                .editSpending("Обучение Advanced 2.0")
                .setNewSpendingDescription(newDescription)
                .saveSpending();
        new MainPage().getSpendingTable()
                .checkTableContains(newDescription);
    }
    @User
    @Test
    void shouldAddNewSpending(UserJson user) {
        String category = "Friends";
        int amount = 100;
        Date currentDate = new Date();
        String description = RandomDataUtils.randomSentence(3);
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(currentDate)
                .setNewSpendingDescription(description)
                .saveSpending()
                .checkAlertMessage("New spending is successfully created");
        new MainPage().getSpendingTable()
                .checkTableContains(description);
    }
    @User
    @Test
    void shouldNotAddSpendingWithEmptyCategory(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingAmount(100)
                .setNewSpendingDate(new Date())
                .saveSpending()
                .checkFormErrorMessage("Please choose category");
    }
    @User
    @Test
    void shouldNotAddSpendingWithEmptyAmount(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingCategory("Friends")
                .setNewSpendingDate(new Date())
                .saveSpending()
                .checkFormErrorMessage("Amount has to be not less then 0.01");
    }
    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void deleteSpendingTest(UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getSpendingTable()
                .deleteSpending("Обучение Advanced 2.0")
                .checkTableSize(0);
    }
    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
        StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getStatComponent();
        Thread.sleep(3000);
        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");
        statComponent.checkBubblesColor(Color.yellow);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void checkStatBubbleContent(UserJson user) throws InterruptedException {
        StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getStatComponent();
        Thread.sleep(3000);
        Bubble bubble = new Bubble(Color.yellow, "Обучение 79990 ₽");
        statComponent.checkBubbles(bubble);
    }

    @User(
            categories = {
                    @Category(name = "Обучение"),
                    @Category(name = "Развлечения")
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 1000
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Поход в кино",
                            amount = 100
                    )
            }
    )
    @Test
    void checkStatBubblesInAnyOrder(UserJson user) throws InterruptedException {
        StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getStatComponent();
        Thread.sleep(3000);
        Bubble bubble1 = new Bubble(Color.yellow, "Обучение 1000 ₽");
        Bubble bubble2 = new Bubble(Color.green, "Развлечения 100 ₽");
        statComponent.checkBubblesInAnyOrder(bubble2, bubble1);
    }

    @User(
            categories = {
                    @Category(name = "Обучение"),
                    @Category(name = "Развлечения")
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 1000
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Поход в кино",
                            amount = 100
                    )
            }
    )
    @Test
    void checkStatBubbleContainsAmongOtherBubbles(UserJson user) throws InterruptedException {
        StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getStatComponent();
        Thread.sleep(3000);
        Bubble bubble = new Bubble(Color.yellow, "Обучение 1000 ₽");
        statComponent.checkBubblesContains(bubble);
    }

    @User(
            categories = {
                    @Category(name = "Обучение"),
                    @Category(name = "Развлечения")
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 1000
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Поход в кино",
                            amount = 100
                    )
            }
    )
    @Test
    void checkSpendsExistInTable(UserJson user) throws InterruptedException {
        SpendingTable spendingTable = Selenide.open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage())
                .getSpendingTable();
        Thread.sleep(3000);
        // Извлекаем список SpendJson и передаем его в checkSpendingTable
        List<SpendJson> expectedSpends = user.testData().spends();
        spendingTable.checkSpendingTable(expectedSpends.toArray(new SpendJson[0]));
    }
}