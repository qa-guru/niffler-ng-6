package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTable {

    private final SelenideElement self = $("#spendings");
    private final SearchField searchField = new SearchField($("input[placeholder='Search']"));

    public SpendingTable selectPeriod(DataFilterValues period) {
        self.$("#period").click();
        $("#menu-period li[data-value='" + period + "']").click();
        return this;
    }

    public EditSpendingPage editSpending(String description) {
        searchField.search(description);
        self.$$("td").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public SpendingTable deleteSpending(String description) {
        self.$$("td").find(text(description)).$$("td").get(0).click();
        self.$("#delete").click();
        return this;
    }

    public SpendingTable searchSpendingByDescription(String description) {
        searchField.search(description);
        return this;
    }

    public SpendingTable checkTableContains(String... expectedSpends) {
        for (String expectedSpend : expectedSpends) {
            searchField.search(expectedSpend);
            self.$$("td").find(text(expectedSpend)).should(visible);
        }
        return this;
    }

    public SpendingTable checkTableSize(int expectedSize) {
        self.$$("tr").shouldHave(size(expectedSize));
        return this;
    }

    public enum DataFilterValues {
        ALL, MONTH, WEEK, TODAY
    }
}
