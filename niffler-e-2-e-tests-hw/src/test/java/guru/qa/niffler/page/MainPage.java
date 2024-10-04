package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.spending.AddNewSpendingPage;
import guru.qa.niffler.page.spending.EditSpendingPage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@Slf4j
public class MainPage {

    private final SelenideElement statisticsTitle = $(byText("Statistics")).as("Statistics title"),
            statisticsBar = $("canvas").as("Statistics bar"),
            historyOfSpendingsTitle = $(byText("History of Spendings")).as("History of Spendings title"),
            spendingsSearchInputContainer = historyOfSpendingsTitle.parent().$("form").as("Spendings search input container"),
            spendingsSearchInput = $("[placeholder='Search']").as("Spendings search input"),
            spendingsSearchInputCleanButton = $("#input-clear").as("Spendings search input clean button"),
            spendingsPeriodSelector = $("#period").as("Spendings period selector"),
            currencySelector = $("#currency").as("Currency selector"),
            deleteSpendingButton = $("#delete").as("Delete spending button"),
            noSpendingsTitle = $(byText("There are no spendings")).as("There are no spendings title"),
            noSpendingsImage = $("[alt='Lonely niffler']").as("Lonely niffler/No spendings image");

    private final ElementsCollection spendingRows = $$("#spendings tbody tr"),
            statisticsLegend = $$("#legend-container li").as("Statistics items list"),
            spendingsPeriodList = $$("#menu-period li").as("Spendings period list"),
            currenciesList = $$("#menu-currency li").as("Currencies list");

    public AppHeader getHeader() {
        return new AppHeader();
    }

    public MainPage searchSpending(@NonNull String query) {
        log.info("Filtering spendings by query {}", query);
        spendingsSearchInputContainer.click();
        spendingsSearchInput.shouldBe(visible).setValue(query).pressEnter();
        return this;
    }

    public MainPage createNewSpending(@NonNull SpendJson spend) {
        log.info("Go to 'Create new spending' page");
        getHeader().goToCreateSpendingPage();
        return new AddNewSpendingPage().createNewSpending(spend);
    }

    public EditSpendingPage openEditSpendingPage(@NonNull String spendingName) {
        searchSpending(spendingName);
        getSpendingInCurrentSearchPage(spendingName, 0).$$("td").get(5)
                .as("['Spending " + spendingName + "' edit button]").click();
        return new EditSpendingPage();
    }

    /**
     * Filter spendings by text and open edit spending page by index (min = 0).
     * Newest spendings on top of the spending table in main page.
     */
    public EditSpendingPage openEditSpendingPage(@NonNull String spendingName, int index) {
        searchSpending(spendingName);
        getSpendingInCurrentSearchPage(spendingName, index).$$("td").get(5)
                .as("['Spending " + spendingName + "' edit button]").click();
        return new EditSpendingPage();
    }

    private SelenideElement getSpendingInCurrentSearchPage(@NonNull String spendingDescription, int index) {
        return spendingRows.filter(text(spendingDescription)).get(index);
    }

    public void checkThatTableContainsSpending(@NonNull String spendingDescription) {
        spendingRows.find(text(spendingDescription)).should(visible);
    }

    public void assertMainPageAndElementsAreVisible() {
        statisticsTitle.shouldBe(visible);
        historyOfSpendingsTitle.shouldBe(visible);
    }

    public MainPage assertSpendingExists(@NonNull SpendJson spend) {
        var description = spend.description();
        searchSpending(description);
        $x("//*[@id='spendings']//tr//td[4]//span[text()='" + description + "']").as("Spending [" + description + "] description").shouldBe(visible);
        return this;
    }

}
