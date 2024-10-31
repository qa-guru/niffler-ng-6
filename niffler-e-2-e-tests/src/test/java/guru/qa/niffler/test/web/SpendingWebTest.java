package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.SignInPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "duck",
            spending = @Spending(
                    category = "Spending Test Category",
                    description = "Spending Test Description",
                    amount = 3174.58
            )
    )
//    An example of DisabledByIssue annotation usage
//    @DisabledByIssue("2")
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn("duck", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        // ? This can create false positive results with multiple executions
        new MainPage().checkThatTableContainsSpending(newDescription);
    }
}
