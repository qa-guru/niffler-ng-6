package guru.qa.niffler.page.component;

import guru.qa.niffler.condition.SpendCondition;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTable extends BaseComponent<SpendingTable> {

    private final SearchField searchField = new SearchField($("input[placeholder='Search']"));

    public SpendingTable() {
        super($("#spendings"));
    }

    public SpendingTable selectPeriod(DataFilterValues period) {
        self.$("#period").click();
        $("#menu-period li[data-value='" + period + "']").click();
        return this;
    }

    public EditSpendingPage editSpending(String description) {
        searchField.search(description);
        self.$$("tbody tr").find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public SpendingTable deleteSpending(String description) {
        self.$$("tbody tr").find(text(description)).$$("td").get(0).click();
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

    @Step("Check what spends match")
    public SpendingTable checkSpendingMatch(SpendJson ...expectedSpends) {
        self.$$("tbody tr").should(SpendCondition.spends(expectedSpends));
        return new SpendingTable();
    }

    public enum DataFilterValues {
        ALL, MONTH, WEEK, TODAY
    }
}
