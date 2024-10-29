package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extantion.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
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

    @Test
    void createSpending() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa", "12345")
                .openNewSpending()
                .createNewSpending();
    }
}