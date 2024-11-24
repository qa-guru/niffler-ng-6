package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.conditions.Bubble;
import guru.qa.niffler.conditions.Color;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.MainPage;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@WebTest
class SpendingWebTests {

    @Test
    void canCreateSpendingTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser
            UserJson user
    ) {

        var spend = SpendUtils.generate();

        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(spend)
                .shouldHaveSpends(spend)
                .shouldBeSuccessAlert()
                .shouldHaveMessageAlert("New spending is successfully created")
        ;

    }

    @Test
    void canEditSpendingTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    spendings = @Spending
            )
            UserJson user
    ) {

        var newSpend = SpendUtils.generate();

        Selenide.open(MainPage.URL, MainPage.class)
                .openEditSpendingPage(user.getTestData().getSpendings().getFirst())
                .editSpending(newSpend)
                .shouldBeSuccessAlert()
                .openEditSpendingPage(newSpend)
                .shouldHaveData(newSpend);

    }

    @Test
    void canCreateNewSpendingWithExistsDescriptionTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    spendings = @Spending
            )
            UserJson user
    ) {
        var spend = user.getTestData().getSpendings().getFirst();
        Selenide.open(MainPage.URL, MainPage.class)
                .createNewSpending(spend)
                .shouldVisiblePageElements()
                .shouldContainsSpends(spend, spend); // checking table contains 2 spends with same data
    }

    @Test
    void testForConditions(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    currency = CurrencyValues.RUB
            )
            UserJson user
    ) {

        SpendJson spend1 = SpendUtils.generate().setCurrency(CurrencyValues.RUB),
                spend2 = SpendUtils.generate().setCurrency(CurrencyValues.RUB),
                spend3 = SpendUtils.generate().setCurrency(CurrencyValues.RUB),
                spend4 = SpendUtils.generate().setCurrency(CurrencyValues.RUB);

        List<SpendJson> spends = new ArrayList<>();
        spends.add(spend1);
        spends.add(spend2);
        spends.add(spend3);
        spends.add(spend4);

        spends.sort(Comparator.comparing(SpendJson::getAmount, Comparator.reverseOrder()));

        Bubble bubble1 = new Bubble(Color.YELLOW, spends.get(0).getCategory().getName() + " " + spends.get(0).getAmountWithSymbol()),
                bubble2 = new Bubble(Color.GREEN, spends.get(1).getCategory().getName() + " " + spends.get(1).getAmountWithSymbol()),
                bubble3 = new Bubble(Color.ORANGE, spends.get(2).getCategory().getName() + " " + spends.get(2).getAmountWithSymbol()),
                bubble4 = new Bubble(Color.BLUE100, spends.get(3).getCategory().getName() + " " + spends.get(3).getAmountWithSymbol());

        Selenide.open(MainPage.URL, MainPage.class)
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