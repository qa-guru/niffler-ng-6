package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendingTable {
    private final SelenideElement spends = $(".MuiTableContainer-root");
    private static final ElementsCollection timePeriods = $$("[role='option']");

    private static final String deleteConfirmButton
            = ".MuiPaper-root button.MuiButtonBase-root.MuiButton-containedPrimary";
    private static final String spendingRow = "tr";

    private final SearchField searchField = new SearchField();

    @Step("Выбор периода для отображения трат периода: {period}")
    public SpendingTable selectPeriod(String period) {
        spends.$("#period").click();
        timePeriods.find(text(period)).click();
        return this;
    }

    @Step("Изменения описания траты на: {spendingDescription}")
    public EditSpendingPage editSpending(String description) {
        spends.$$(spendingRow).find(text(description)).$(" [aria-label='Edit spending']").click();
        return new EditSpendingPage();
    }

    @Step("Удаление траты с описанием: {description}")
    public SpendingTable deleteSpending(String description) {
        spends.$$(spendingRow).find(text(description)).$$("td").get(1).click();
        spends.$("#delete").shouldBe(visible).click();
        $(deleteConfirmButton).shouldBe(visible).click();
        return this;
    }

    @Step("Поиск траты с описанием: {description}")
    public SpendingTable searchSpendingByDescription(String description) {
        searchField.search(description);
        return this;
    }

    @Step("Проверка, что таблица содержит трату: {expectedSpends}")
    public SpendingTable checkTableContains(String... expectedSpends) {
        spends.$$("td:nth-child(4)").shouldHave(textsInAnyOrder(expectedSpends));
        return this;
    }

    @Step("Проверка? что количество трат равно: {expectedSize}")
    public SpendingTable checkTableSize(int expectedSize) {
        spends.$(spendingRow).$$("tr").shouldHave(size(expectedSize));
        return this;
    }
}
