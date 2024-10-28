package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.CreateNewUserExtension;
import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.auth.LoginPage;
import guru.qa.niffler.utils.SpendUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({
        CreateNewUserExtension.class,
        CategoryExtension.class,
        SpendingExtension.class
})
class SpendingWebTests {

    static final String LOGIN_PAGE_URL = Config.getInstance().authUrl() + "login";

    @Test
    void canCreateSpendingTest(@CreateNewUser UserJson user) {

        var spend = SpendUtils.generate();

        open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend)
                .shouldHaveSpend(spend);

    }

    @Test
    void canEditSpendingTest(@CreateNewUser(spendings = @Spending) UserJson user) {

        var newSpend = SpendUtils.generate();

        open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .openEditSpendingPage(user.getTestData().getSpendings().getFirst())
                .editSpending(newSpend)
                .openEditSpendingPage(newSpend)
                .shouldHaveData(newSpend);

    }

    @Test
    @Spending
    void canCreateNewSpendingWithExistsDescriptionTest(@CreateNewUser(spendings = @Spending) UserJson user) {
        var spend = user.getTestData().getSpendings().getFirst();
        open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .createNewSpending(spend)
                .shouldVisiblePageElements()
                .shouldHaveSpends(spend, 2);
    }

}