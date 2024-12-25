package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

@ExtendWith(BrowserExtension.class)
@Order(2)
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "esa",
            categories = @Category(
                    archived = false
            ),
            spendings = @Spending(
                    category = "Обучение3",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    // @DisableByIssue("1")
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";
        //test pull request
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription);
        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void createSpending() {
        SpendJson spend = new SpendJson(null, new Date(), null, null, 555.0, RandomDataUtils.randomName(), null);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345")
                .openNewSpending()
                .createNewSpending(spend)
                .checkAlert("New spending is successfully created");
    }

    @User(
            spendings = {@Spending(
                    category = "work",
                    description = "Education",
                    amount = 3000
            ),
                    @Spending(
                            category = "entertainment",
                            description = "Move",
                            amount = 500
                    )}

    )
    @ScreenShotTest(value = "img/expected-stat.png")
    void checkStatComponentTest(BufferedImage expected, UserJson user) throws IOException, InterruptedException {
        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getStatComponent();


//        Assertions.assertFalse(new ScreenDiffResult(
//                expected,
//                statComponent.chartScreenshot()
//        ) ,"Screen comparison failure"
//        );
        Bubble[] expectedBubbles = new Bubble[user.testData().spends().size()];
        List<Color> colors = List.of(Color.values());
        int i = 0;
        for (SpendJson spendJson : user.testData().spends()) {
            expectedBubbles[i] = new Bubble(colors.get(i), String.format("%.0f", spendJson.amount()));
            i++;
        }

        statComponent.checkBubbles(expectedBubbles);

    }


    @User(
            spendings = {@Spending(
                    category = "work",
                    description = "Education",
                    amount = 3000
            ),
                    @Spending(
                            category = "entertainment",
                            description = "Move",
                            amount = 500
                    )}

    )
    @Test
    void checkSpendingTable(UserJson user) {
        SpendingTable spendingTable = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getSpendingTable();

        spendingTable.checkSpendingMatch(user.testData().spends().toArray(new SpendJson[0]));

    }

    @User(
            spendings = @Spending(
                    category = "work",
                    description = "Education",
                    amount = 3000
            )
    )
    @ScreenShotTest(value = "img/stat-edit.png")
    void checkStatComponentAfterEditSpendingTest(BufferedImage expected, UserJson user) throws IOException, InterruptedException {
        String newAmount = "2000";
        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending(user.testData().spends().get(0).description())
                .setNewSpendingAmount(newAmount)
                .checkValueCategory(user.testData().spends().get(0).category().name(), newAmount)
                .getStatComponent();

        Thread.sleep(5000);

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");
    }

    @User(
            spendings = {@Spending(
                    category = "work",
                    description = "Education",
                    amount = 3000
            ),
                    @Spending(
                            category = "entertainment",
                            description = "Move",
                            amount = 500
                    )}

    )
    @ScreenShotTest(value = "img/stat-delete.png")
    void checkStatComponentAfterDeleteSpendingTest(BufferedImage expected, UserJson user) throws IOException, InterruptedException {
        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .deleteSpending(user.testData().spends().get(1).description())
                .getStatComponent();
        Thread.sleep(5000);

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");
    }

    @User(
            spendings = @Spending(
                    category = "work",
                    description = "Education",
                    amount = 3000
            )
    )
    @ScreenShotTest(value = "img/stat-archiv.png")
    void checkStatComponentAfterArchivedCategoryTest(BufferedImage expected, UserJson user) throws IOException, InterruptedException {
        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openProfilePage()
                .archivedCategory(user.testData().spends().get(0).category().name())
                .returnToMainPage()
                .checkValueCategory("Archived", "3000")
                .getStatComponent();

        Thread.sleep(5000);
        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");
    }
}