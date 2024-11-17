package guru.qa.niffler.test.web;

import guru.qa.niffler.conditions.Bubble;
import guru.qa.niffler.conditions.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.CreateNewUserExtension;
import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.auth.LoginPage;
import guru.qa.niffler.utils.SpendUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({
        CreateNewUserExtension.class,
        CategoryExtension.class,
        SpendingExtension.class
})
@WebTest
class SpendingWebTests {

    static final String LOGIN_PAGE_URL = Config.getInstance().authUrl() + "login";

    @Test
    void canCreateSpendingTest(@CreateNewUser UserJson user) {

        var spend = SpendUtils.generate();

        open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend)
                .shouldHaveSpends(spend)
                .shouldBeSuccessAlert()
                .shouldHaveMessageAlert("New spending is successfully created")
        ;

    }

    @Test
    void canEditSpendingTest(@CreateNewUser(spendings = @Spending) UserJson user) {

        var newSpend = SpendUtils.generate();

        open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .openEditSpendingPage(user.getTestData().getSpendings().getFirst())
                .editSpending(newSpend)
                .shouldBeSuccessAlert()
                .openEditSpendingPage(newSpend)
                .shouldHaveData(newSpend);

    }

    @Test
    void canCreateNewSpendingWithExistsDescriptionTest(@CreateNewUser(spendings = @Spending) UserJson user) {
        var spend = user.getTestData().getSpendings().getFirst();
        open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend)
                .shouldVisiblePageElements()
                .shouldContainsSpends(spend, spend); // checking table contains 2 spends with same data
    }


    @Test
    void testForConditions(@CreateNewUser(currency = CurrencyValues.RUB) UserJson user) {

        SpendJson spend1 = SpendUtils.generate().setCurrency(CurrencyValues.RUB),
                spend2 = SpendUtils.generate().setCurrency(CurrencyValues.RUB),
                spend3 = SpendUtils.generate().setCurrency(CurrencyValues.RUB),
                spend4 = SpendUtils.generate().setCurrency(CurrencyValues.RUB);

        List<SpendJson> spends = new ArrayList<>();
        spends.add(spend1);
        spends.add(spend2);
        spends.add(spend3);
        spends.add(spend4);

        System.out.println("Before: " + spends);
        spends.sort(Comparator.comparing(SpendJson::getAmount, Comparator.reverseOrder()));
        System.out.println("After: " + spends);

        Bubble bubble1 = new Bubble(Color.YELLOW, spends.get(0).getCategory().getName() + " " + spends.get(0).getAmountWithSymbol()),
                bubble2 = new Bubble(Color.GREEN, spends.get(1).getCategory().getName() + " " + spends.get(1).getAmountWithSymbol()),
                bubble3 = new Bubble(Color.ORANGE, spends.get(2).getCategory().getName() + " " + spends.get(2).getAmountWithSymbol()),
                bubble4 = new Bubble(Color.BLUE100, spends.get(3).getCategory().getName() + " " + spends.get(3).getAmountWithSymbol());

        open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend1)
                .createNewSpending(spend2)
                .createNewSpending(spend3)
                .createNewSpending(spend4)
                .shouldVisiblePageElements()
                .shouldHaveStatBubbles(
                        bubble1, bubble3, bubble2, bubble4
                );
    }

}