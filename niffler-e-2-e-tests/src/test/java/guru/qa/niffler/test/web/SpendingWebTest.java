package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extantion.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

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

    @User
    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest(BufferedImage expected) throws IOException {
        SpendJson spend = new SpendJson(null, new Date(), null, null, 555.0, RandomDataUtils.randomName(), null);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345");

        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());

        Assertions.assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));
    }

}