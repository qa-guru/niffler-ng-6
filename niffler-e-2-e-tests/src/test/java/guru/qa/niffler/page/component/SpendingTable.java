package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendingTable<T extends BasePage<?>> extends BaseComponent<T> {

    private static final ElementsCollection timePeriods = $$("[role='option']");
    private final SelenideElement deleteButton = $("#delete");

    private static final String deleteConfirmButton = ".MuiDialogActions-spacing [type='button']:nth-child(2)";
    private static final String spendingRow = "tbody tr";
    private static final String spendingColumn = "td:nth-child(4)";

    public SpendingTable(SelenideElement spends, T page) {
        super(spends, page);
    }

    @Step("Выбор периода для отображения трат: {period}")
    public SpendingTable<T> selectPeriod(String period) {
        self.$("#period").click();
        timePeriods.find(text(period)).click();
        return this;
    }

    @Step("Изменения описания траты на: {spendingDescription}")
    public EditSpendingPage editSpending(String description) {
        self.$$(spendingRow).find(text(description)).$(" [aria-label='Edit spending']").click();
        return new EditSpendingPage();
    }

    @Step("Удаление траты с описанием: {description}")
    public SpendingTable<T> deleteSpending(String description) {
        self.$(spendingRow).$$("tr").find(text(description)).$$("td").get(1).click();
        deleteButton.shouldBe(visible).click();
        $(deleteConfirmButton).shouldBe(visible).click();
        return this;
    }

    @Step("Поиск траты с описанием: {description}")
    public SpendingTable<T> searchSpendingByDescription(String description) {
        self.$(spendingRow).$$("tr").find(text(description)).shouldBe(visible);
        return this;
    }

    @Step("Проверка, что таблица содержит траты: {expectedSpends}")
    public SpendingTable<T> checkTableContains(String... expectedSpends) {
        self.$(spendingRow).$("td").$$(spendingColumn).shouldHave(textsInAnyOrder(expectedSpends));
        return this;
    }

    @Step("Проверка, что количество трат равно: {expectedSize}")
    public SpendingTable<T> checkTableSize(int expectedSize) {
        self.$(spendingRow).$$("tr").shouldHave(size(expectedSize));
        return this;
    }
}