package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsHeader = $("#stat h2");
    private final SelenideElement historyOfSpendingHeader = $("#spendings h2");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage statisticsHeaderShouldHaveText(String text) {
        statisticsHeader.shouldHave(text(text)).shouldBe(visible);
        return this;
    }

    public MainPage historyOfSpendingHeaderShouldHaveText(String value) {
        historyOfSpendingHeader.shouldHave(text(value)).shouldBe(visible);
        return this;
    }
}
