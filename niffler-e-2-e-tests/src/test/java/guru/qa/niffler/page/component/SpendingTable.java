package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTable {

    private final SelenideElement self = $("spendings");

    public SpendingTable selectPeriod(DataFilterValues period) {
        self.$("#period").click();
        $("#menu-period li[data-value='" + period + "']").click();
        return this;
    }

    public EditSpendingPage editSpending(String description) {
    }

    public SpendingTable deleteSpending(String description) {
        self.$$("td").find(text(description)).$$("td").get(0).click();
        self.$("#delete").click();
        return this;
    }

    public SpendingTable searchSpendingByDescription(String description) {

    }

    public SpendingTable checkTableContains(String... expectedSpends) {
    }

    public SpendingTable checkTableSize(int expectedSize) {
    }

    public enum DataFilterValues {
        ALL, MONTH, WEEK, TODAY
    }
}
