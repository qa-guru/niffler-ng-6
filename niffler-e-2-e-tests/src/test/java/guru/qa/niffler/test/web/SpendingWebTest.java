package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotations.Spending;
import guru.qa.niffler.jupiter.annotations.User;
import guru.qa.niffler.jupiter.annotations.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingWebTest extends BaseWebTest {

    @User(
            username = "duck",
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();
        new MainPage().checkThatTableContainsSpending(newDescription);
    }
}

