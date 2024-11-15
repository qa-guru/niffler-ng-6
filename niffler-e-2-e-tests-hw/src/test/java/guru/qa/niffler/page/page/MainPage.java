package guru.qa.niffler.page.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.conditions.And;
import guru.qa.niffler.enums.Period;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.component.FloatForm;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.ScreenshotComponent;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.page.spending.AddNewSpendingPage;
import guru.qa.niffler.page.page.spending.EditSpendingPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.conditions.SelenideCondition.child;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private final SelenideElement statisticsTitle = $(byText("Statistics")).as("Statistics title"),
            statisticsCanvas = $("canvas[role='img']").as("Spend stats canvas"),
            historyOfSpendingsTitle = $(byText("History of Spendings")).as("History of Spendings title"),
            searchForm = historyOfSpendingsTitle.parent().$("form").as("Spendings search input container"),
            spendingsPeriodSelector = $("#period").as("Spendings period selector"),
            currencySelector = $("#currency").as("Currency selector"),
            deleteSpendingButton = $("#delete").as("Delete spending button"),
            noSpendingsTitle = $(byText("There are no spendings")).as("There are no spendings title"),
            noSpendingsImage = $("[alt='Lonely niffler']").as("Lonely niffler/No spendings image"),
            previousPageButton = $x("//button[text()='Previous']").as("[Spending table 'previous page' button]"),
            nextPageButton = $x("//button[text()='Next']").as("[Spending table 'next page' button]"),
            allSpendingsSelector = $x("//thead/input").as("[All spendings selector]");

    private final ElementsCollection spendingRows = $$("#spendings tbody tr"),
            spendingsLegendList = $$("#legend-container li").as("Statistics items list"),
            spendingsPeriodList = $$("#menu-period li").as("Spendings period list"),
            currenciesList = $$("#menu-currency li").as("Currencies list");

    @Getter
    private final Header header = new Header();
    private final SearchField searchField = new SearchField(searchForm);
    private final FloatForm floatForm = new FloatForm();

    public MainPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    @Step("Filter spendings by description = [{}]")
    public MainPage filterSpendingsByDescription(String query) {
        log.info("Filtering spendings by query {}", query);
        searchForm.click();
        searchField.setValue(query);
        return this;
    }

    @Step("Filter spendings by period = [{}]")
    public MainPage filterSpendingsByPeriod(Period period) {
        log.info("Filtering spendings by period: [{}]", period);
        spendingsPeriodSelector.click();
        spendingsPeriodList.findBy(text(period.getValue())).shouldBe(visible).click();
        return this;
    }

    @Step("Filter spendings by currency = [{}]")
    public MainPage filterSpendingsByCurrency(CurrencyValues currency) {
        log.info("Filtering spendings by currency: [{}]", currency);
        currencySelector.click();
        currenciesList.findBy(text(currency.name())).shouldBe(visible).click();
        return this;
    }

    @Step("Filter spendings by criteria:")
    public MainPage filterSpendings(SpendJson spend) {

        if (spend.getDescription() != null && spend.getDescription().isEmpty()) {
            filterSpendingsByDescription(spend.getDescription());
        }

        if (spend.getSpendDate() != null) {
            filterSpendingsByPeriod(getMinPeriodFromNowToDate(spend.getSpendDate()));
        }

        if (spend.getCurrency() != null)
            filterSpendingsByCurrency(spend.getCurrency());

        return this;

    }

    @Step("Create new spending with name = [{spend.description}]")
    public MainPage createNewSpending(SpendJson spend) {
        log.info("Go to 'Create new spending' page");
        getHeader().goToAddNewSpendingPage();
        return new AddNewSpendingPage(true).createNewSpending(spend);
    }

    @Step("Open edit spending page")
    public EditSpendingPage openEditSpendingPage(String spendDescription, int index) {

        if (index < 0)
            throw new IllegalArgumentException("Index must be greater than 0");

        filterSpendingsByDescription(spendDescription);

        log.info("Open edit spending page with description = [{}] and index = [{}]", spendDescription, index);
        Allure.step("Open edit spending page with description = [" + spendDescription + "] and index = [" + index + "]",
                () -> getSpendingContainer(spendDescription, index).$x(".//button[@aria-label='Edit spending']")
                        .as("['Spending " + spendDescription + "' edit button]").click());

        return new EditSpendingPage();
    }

    @Step("Open edit spending page with spend criteria and index = [{index}]:")
    public EditSpendingPage openEditSpendingPage(SpendJson spend, int index) {

        if (index < 0)
            throw new IllegalArgumentException("Index must be greater than 0");

        var logText = "Open edit spending page with: description = [%s], category = [%s], amount = [%s], date = [%s] and index = [%s]"
                .formatted(
                        spend.getDescription(),
                        spend.getCategory().getName(),
                        getAmountWithCurrencySymbol(spend),
                        new SimpleDateFormat("MMM dd, yyyy"),
                        index);

        filterSpendings(spend);

        log.info(logText);
        Allure.step(logText, () -> getSpendingContainer(spend, index).$x(".//button[@aria-label='Edit spending']")
                .as("['Spending " + spend.getDescription() + "' edit button]").click());

        return new EditSpendingPage();

    }

    public EditSpendingPage openEditSpendingPage(String spendingName) {
        return openEditSpendingPage(spendingName, 0);
    }

    public EditSpendingPage openEditSpendingPage(SpendJson spend) {
        return openEditSpendingPage(spend, 0);
    }

    @Step("Select spending by description = [{}]")
    public MainPage selectSpending(String spendingDescription) {
        filterSpendingsByDescription(spendingDescription);
        log.info("Select spending by description = [{}]", spendingDescription);
        Allure.step("Select spending by description = [" + spendingDescription + "]", () ->
                getSpendingContainer(spendingDescription, 0).$x(".//td[1]/span").shouldBe(visible).click());
        return this;
    }

    @Step("Select spending by criteria = [{}]")
    public MainPage selectSpending(SpendJson spend) {
        filterSpendings(spend);
        String logText = "Select spending with criteria: category = [%s], amount = [%s %s], description = [%s], date = [%s]"
                .formatted(
                        spend.getCategory(),
                        spend.getAmount(),
                        spend.getCurrency().getSymbol(),
                        spend.getDescription(),
                        new SimpleDateFormat("MMM dd, yyyy").format(spend.getSpendDate()));
        log.info(logText);
        Allure.step(logText, () ->
                getSpendingContainer(spend, 0).$x(".//td[1]/span").shouldBe(visible).click());
        return this;
    }

    @Step("Select spendings by criteria")
    public MainPage selectSpendings(List<SpendJson> spends) {
        log.info("Select spendings");
        spends.forEach(this::selectSpending);
        return this;
    }

    @Step("Select all spendings in current spending table page")
    public MainPage selectAllSpendings() {
        log.info("Select all spendings");
        allSpendingsSelector.shouldBe(visible).click();
        return this;
    }

    @Step("Remove selected spendings")
    public MainPage deleteSpendings() {
        log.info("Delete all spendings");
        deleteSpendingButton.shouldBe(visible).click();
        floatForm.submit();
        return this;
    }

    @Step("Go to previous spending table page")
    private MainPage goToPreviousPageOfSpendingsTable() {
        log.info("Go to previous page of spendings table");
        previousPageButton.shouldBe(visible).click();
        return this;
    }

    @Step("Go to next spending table page")
    private MainPage goToNextPageOfSpendingTable() {
        log.info("Go to next page of spendings table");
        nextPageButton.shouldBe(visible).click();
        return this;
    }

    @Step("Should visible spending with description = [{}]")
    public MainPage shouldHaveSpend(String description) {
        filterSpendingsByDescription(description);
        log.info("Should have spend with description = [{}]", description);
        Allure.step("Should have spend with description = [" + description + "]", () ->
                getSpendingContainer(description, 0).shouldBe(visible));
        return this;
    }

    @Step("Should visible spend by criteria")
    public MainPage shouldHaveSpend(SpendJson spend) {

        filterSpendings(spend);

        String logText = "Should visible spend with criteria: category = [%s], amount = [%s %s], description = [%s], date = [%s]"
                .formatted(
                        spend.getCategory(),
                        spend.getAmount(),
                        spend.getCurrency().getSymbol(),
                        spend.getDescription(),
                        new SimpleDateFormat("MMM dd, yyyy").format(spend.getSpendDate()));
        log.info(logText);
        Allure.step(logText, () -> getSpendingContainer(spend, 0).shouldBe(visible));

        return this;
    }

    @Step("Should have spends")
    public MainPage shouldHaveSpends(String description, int count) {
        filterSpendingsByDescription(description);
        log.info("Should visible [{}] spends with description = [{}]", count, description);
        Allure.step("Should visible [" + count + "] spends with description = [" + description + "]", () ->
                spendingRows
                        .filterBy(child(byXpath(".//td[4]//span"), exactText(description)))
                        .shouldHave(size(count))
        );
        return this;
    }

    @Step("Should have [{count}] spends with params")
    public MainPage shouldHaveSpends(SpendJson spend, int count) {

        filterSpendings(spend);

        var spendConditions = spendConditions(spend);
        var logText = "Should visible [%d] spends by: category = [%s], amount = [%s %s], description = [%s], date = [%s]"
                .formatted(
                        count,
                        spend.getCategory().getName(),
                        spend.getAmount(),
                        spend.getAmount(),
                        spend.getDescription(),
                        new SimpleDateFormat("MMM dd, yyy").format(spend.getSpendDate()));

        log.info(logText);
        Allure.step(logText,
                () -> spendingRows
                        .filterBy(new And("have", spendConditions))
                        .shouldHave(size(count)));

        return this;

    }

    @Step("Should visible spend legend = [{description} {amount} {currency.symbol}]")
    public MainPage shouldHaveSpendLegend(String description, Double amount, CurrencyValues currency) {
        log.info("Should visible spend legend = [{} {} {}]", description, amount, currency);
        spendingsLegendList
                .filterBy(text("%s %s %s".formatted(description, amount, currency.getSymbol())))
                .shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Should not have spends")
    public MainPage shouldNotHaveSpends() {
        log.info("Assert spending table not have spends");
        noSpendingsTitle.shouldBe(visible);
        noSpendingsImage.shouldBe(visible);
        return this;
    }

    @SneakyThrows
    @Step("Should expected spend stats equals actual")
    public MainPage shouldHaveSpendsStatCanvas(BufferedImage expected) {
        log.info("Should expected spend stats equals actual");
        ScreenshotComponent.validateElement(statisticsCanvas, expected);
        return this;
    }

    @SneakyThrows
    @Step("Should expected spend stats equals actual")
    public MainPage shouldHaveSpendsStatCanvas(BufferedImage expected, double diffPercent) {
        log.info("Should expected spend stats equals actual");
        ScreenshotComponent.validateElement(statisticsCanvas, expected);
        return this;
    }

    @Override
    public MainPage shouldVisiblePageElement() {
        log.info("Assert 'Main' page element visible on start up");
        historyOfSpendingsTitle.shouldBe(visible);
        return this;
    }


    @Override
    @Step("Should visible main page")
    public MainPage shouldVisiblePageElements() {

        log.info("Assert account menu elements are visible");

        statisticsTitle.shouldBe(visible);
        statisticsCanvas.shouldBe(visible);

        searchForm.shouldBe(visible);
        spendingsPeriodSelector.shouldBe(visible);
        currencySelector.shouldBe(visible);

        return this;

    }

    private SelenideElement getSpendingContainer(String spendingDescription, int index) {
        return spendingRows.filter(child(byXpath(".//td[4]/span"), exactText(spendingDescription))).get(index);
    }

    private SelenideElement getSpendingContainer(SpendJson spend, int index) {
        return spendingRows.filterBy(new And("have", spendConditions(spend))).get(index);
    }

    private List<WebElementCondition> spendConditions(SpendJson spend) {
        List<WebElementCondition> spendCondition = new ArrayList<>();
        if (spend.getCategory().getName() != null)
            spendCondition.add(child(byXpath(".//td[2]/span"), exactText(spend.getCategory().getName())));
        if (spend.getAmount() != null)
            spendCondition.add(child(byXpath(".//td[3]/span"), exactText(getAmountWithCurrencySymbol(spend))));
        if (spend.getDescription() != null)
            spendCondition.add(child(byXpath(".//td[4]/span"), exactText(spend.getDescription())));
        if (spend.getSpendDate() != null)
            spendCondition.add(child(byXpath(".//td[5]/span"), exactText(new SimpleDateFormat("MMM dd, yyyy").format(spend.getSpendDate()))));

        return spendCondition;
    }

    private Period getMinPeriodFromNowToDate(Date date) {
        LocalDate today = LocalDate.now(),
                week = today.minusWeeks(1),
                month = today.minusMonths(1),
                sourceDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (sourceDate == today) return Period.TODAY;
        if (sourceDate.isAfter(week) || sourceDate == week) return Period.LAST_WEEK;
        if (sourceDate.isAfter(month) || sourceDate == month) return Period.LAST_MONTH;
        return Period.ALL_TIME;
    }

    private String getAmountWithCurrencySymbol(SpendJson spend) {
        return "%s %s".formatted(
                (spend.getAmount() % 1 != 0)
                        ? spend.getAmount()
                        : spend.getAmount().intValue(), spend.getCurrency().getSymbol());
    }

}