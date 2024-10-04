package guru.qa.niffler.page.spending;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.AppHeader;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.CalendarService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public abstract class SpendingPage<T> {

    protected final SelenideElement title = $("form h2").as("[Page title]"),
            amountLabel = $("label[for='amount']").as("['Amount' label]"),
            amountInput = $("#amount").as("'[Amount' input]"),
            currencyLabel = $("label[for='currency']").as("['Currency' label]"),
            currencySelector = $("#currency").as("['Currency' selector]"),
            currencySelectorText = currencySelector.$x(".//span[2]").as("['Currency' selector text]"),
            currencyInput = $("input[name='currency']").as("['Currency' input]"),
            categoryLabel = $("label[for='category']").as("['Category' label]"),
            categoryInput = $("#category").as("['Category' input]"),
            dateLabel = $("label[for='date']").as("['Currency' label]"),
            dateInput = $("input[name='date']").as("['Date' input]"),
            dateCalendarButton = $x("//button[./*[@alt='Calendar']]").as("['Date' calendar button]"),
            descriptionInput = $("#description").as("['Description' input]"),
            descriptionLabel = $("label[for='description']").as("['Description' label]"),
            cancelButton = $("#cancel").as("['Cancel' button]"),
            saveButton = $("#save").as("['Add' button]");

    protected final ElementsCollection currenciesList = $$("#menu-currency li").as("'Currency' list"),
            categoryTagsList = $$x("//*[./label[@for='category']]//*[@role='button']").as("'Category' list");

    private final CalendarService calendarService = new CalendarService();

    public AppHeader getHeader() {
        return new AppHeader();
    }

    @SuppressWarnings("unchecked")
    public T fillSpendingData(@NonNull SpendJson spend) {

        log.info("Fill spending data: {}", spend);

        setAmount(spend.getAmount());
        selectCurrency(spend.getCurrency());
        setCategory(spend.getCategory().getName());
        setDate(spend.getSpendDate());
        setDescription(spend.getDescription());

        return (T) this;

    }

    // INFO: Amount
    public double getAmount() {
        var amountText = amountInput.getValue();
        return Double.parseDouble(Objects.requireNonNull(amountText).contains(".") ? amountText : amountText + ".0");
    }

    @SuppressWarnings("unchecked")
    public T setAmount(@NonNull Double amount) {
        log.info("Set amount: [{}]", amount);
        amountInput.setValue(amount.toString());
        return (T) this;
    }

    // INFO: Currency
    public CurrencyValues getCurrency() {
        var currencyText = currencySelectorText.as("['Currency' selector text]").shouldBe(visible).getText();
        return Arrays.stream(CurrencyValues.values())
                .filter(currency -> currency.name().equalsIgnoreCase(currencyText))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public T selectCurrency(@NonNull CurrencyValues currency) {
        log.info("Select currency: [{}]", currency);
        currencySelector.click();
        currenciesList.find(text(currency.toString())).click();
        return (T) this;
    }

    // INFO: Category
    public List<String> getCategoryTagList() {
        log.info("Get category tag list");
        // @formatter:off
        return (categoryTagsList.isEmpty())
                ? new ArrayList<>()
                : categoryTagsList.asFixedIterable().stream()
                        .map(category -> category.$x(".//span[1]").getText())
                        .toList();
        // @formatter:on
    }

    @SuppressWarnings("unchecked")
    public T setCategory(@NonNull String categoryName) {
        log.info("Set category: [{}]", categoryName);
        categoryInput.setValue(categoryName).pressEnter();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T selectCategoryFromTags(@NonNull String categoryName) {
        log.info("Select category from tags: [{}]", categoryName);
        categoryTagsList.find(exactText(categoryName)).click();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T categoryTagShouldBeSelected(@NonNull String categoryName) {
        log.info("Category tag should be selected: [{}]", categoryName);
        categoryTagsList.find(exactText(categoryName)).shouldHave(cssClass("MuiChip-colorPrimary"), Duration.ofSeconds(4));
        return (T) this;
    }

    // INFO: Date
    @SuppressWarnings("unchecked")
    public T setDate(Date date) {

        var dateVal = new SimpleDateFormat("MM/dd/yyyy").format(date);
        log.info("Set date: [{}]", dateVal);

        dateInput.click();
        dateInput.sendKeys(Keys.CONTROL + "a");
        dateInput.sendKeys(Keys.BACK_SPACE);
        dateInput.sendKeys(dateVal);
        title.click();
        return (T) this;

    }

    @SuppressWarnings("unchecked")
    public T selectDateFromCalendar(Date date) {
        log.info("Pick date from calendar: [{}]", date);
        calendarService.pickDate(date);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T dateInputShouldHaveDate(Date date) {
        calendarService.calendarInputShouldHaveDate(date);
        return (T) this;
    }

    // INFO: description

    public String getDescription() {
        return descriptionInput.getValue();
    }

    @SuppressWarnings("unchecked")
    public T setDescription(String description) {
        log.info("Set description: [{}]", description);
        descriptionInput.setValue(description);
        return (T) this;
    }

    // INFO: Cancel and Submit
    public MainPage cancel() {
        cancelButton.click();
        return new MainPage();
    }

    public MainPage submit() {
        log.info("Submit spend");
        saveButton.click();
        return new MainPage();
    }

    protected void assertSpendingPageElementsAreVisible() {
        title.shouldBe(visible);
        amountLabel.shouldBe(visible);
        amountInput.shouldBe(visible);
        currencyLabel.shouldBe(visible);
        currencySelector.shouldBe(visible);
        currencyInput.shouldBe(visible);
        categoryLabel.shouldBe(visible);
        categoryInput.shouldBe(visible);
        dateLabel.shouldBe(visible);
        dateInput.shouldBe(visible);
        dateCalendarButton.shouldBe(visible);
        descriptionInput.shouldBe(visible);
        descriptionLabel.shouldBe(visible);
        cancelButton.shouldBe(visible);
        saveButton.shouldBe(visible);
    }

    public abstract T assertPageElementsAreVisible();

    public void spendingShouldHaveAmount(Double amount) {
        var amountVal = String.valueOf((amount % 1.0 == 0) ? amount.intValue() : amount);
        amountInput.shouldHave(value(amountVal));
    }

    public void spendingShouldHaveCurrency(CurrencyValues currency) {
        currencySelector.shouldHave(text(currency.name()));
    }

    public void spendingShouldHaveCategory(String name) {
        categoryTagsList.find(cssClass("MuiChip-colorPrimary")).$x(".//*[contains(@class, 'label')]").shouldHave(text(name));
    }

    public void spendingShouldHaveDate(Date date) {
        var dateVal = new SimpleDateFormat("MM/dd/yyyy").format(date);
        dateInput.shouldHave(value(dateVal));
    }

    public void spendingShouldHaveDescription(String description) {
        descriptionInput.shouldHave(value(description));
    }

    @SuppressWarnings("unchecked")
    public T assertSpendingData(SpendJson spend) {
        log.info("Assert spend: [{}]", spend.getUsername());
        spendingShouldHaveAmount(spend.getAmount());
        spendingShouldHaveCurrency(spend.getCurrency());
        spendingShouldHaveCategory(spend.getCategory().getName());
        spendingShouldHaveDate(spend.getSpendDate());
        spendingShouldHaveDescription(spend.getDescription());
        return (T) this;
    }
}
