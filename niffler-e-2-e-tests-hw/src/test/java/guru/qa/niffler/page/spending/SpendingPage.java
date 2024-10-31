package guru.qa.niffler.page.spending;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.AppHeader;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.CalendarWebService;
import guru.qa.niffler.page.MainPage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@Slf4j
@NoArgsConstructor
public abstract class SpendingPage<T> extends BasePage<T> {

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

    private final CalendarWebService calendarService = new CalendarWebService();

    protected SpendingPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    public AppHeader getHeader() {
        return new AppHeader();
    }

    private SelenideElement getCategorySelector(@Nonnull String categoryName) {
        return categoryTagsList.find(exactText(categoryName));
    }

    @SuppressWarnings("unchecked")
    public T fillSpendingData(@Nonnull SpendJson spend) {

        log.info("Fill spending data: {}", spend);

        setAmount(spend.getAmount());
        selectCurrency(spend.getCurrency());
        setCategory(spend.getCategory().getName());
        setDate(spend.getSpendDate());
        setDescription(spend.getDescription());

        return (T) this;

    }

    @SuppressWarnings("unchecked")
    public T setAmount(@Nonnull Double amount) {
        log.info("Set amount: [{}]", amount);
        amountInput.setValue(amount.toString());
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setCategory(@Nonnull String categoryName) {
        log.info("Set category: [{}]", categoryName);
        categoryInput.setValue(categoryName).pressEnter();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T selectCurrency(@Nonnull CurrencyValues currency) {
        log.info("Select currency: [{}]", currency);
        currencySelector.click();
        currenciesList.find(text(currency.toString())).click();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T selectCategoryFromTags(@Nonnull String categoryName) {
        log.info("Select category from tags: [{}]", categoryName);
        categoryTagsList.find(exactText(categoryName)).click();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setDate(@Nonnull Date date) {

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
    public T selectDateFromCalendar(@Nonnull Date date) {
        log.info("Pick date from calendar: [{}]", date);
        calendarService.pickDate(date);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setDescription(@Nonnull String description) {
        log.info("Set description: [{}]", description);
        descriptionInput.setValue(description);
        return (T) this;
    }

    public MainPage submit() {
        log.info("Submit spend");
        saveButton.click();
        return new MainPage();
    }

    public MainPage cancel() {
        cancelButton.click();
        return new MainPage();
    }

    @SuppressWarnings("unchecked")
    public T shouldHaveAmount(@Nonnull Double amount) {
        var amountVal = String.valueOf((amount % 1.0 == 0) ? amount.intValue() : amount);
        log.info("Spending should have amount: [{}] ", amountVal);
        amountInput.shouldHave(value(amountVal));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldHaveCurrency(@Nonnull CurrencyValues currency) {
        log.info("Spending should have selected currency: [{}]", currency);
        currencySelectorText.shouldHave(text(currency.name()));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldBeSelectedCategory(@Nonnull String categoryName) {
        log.info("Category tag should be selected: [{}]", categoryName);
        getCategorySelector(categoryName).shouldHave(cssClass("MuiChip-colorPrimary"));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldNotBeSelectedCategory(@Nonnull String categoryName) {
        log.info("Category should not be selected: [{}]", categoryName);
        getCategorySelector(categoryName).shouldNotHave(cssClass("MuiChip-colorPrimary"));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldHaveCategory(@Nonnull String category) {
        log.info("Spending should have category: [{}]", category);
        categoryInput.shouldHave(value(category));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T shouldHaveDate(@Nonnull Date date) {
        calendarService.calendarInputShouldHaveDate(date);
        return (T) this;
    }

    // INFO: description
    @SuppressWarnings("unchecked")
    public T shouldHaveDescription(@Nonnull String description) {
        descriptionInput.shouldHave(value(description));
        return (T) this;
    }

    protected void shouldVisibleSpendingPageElements() {
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

    @SuppressWarnings("unchecked")
    public T shouldHaveData(SpendJson spend) {
        log.info("Assert spend: [{}]", spend.getUsername());
        shouldHaveAmount(spend.getAmount());
        shouldHaveCurrency(spend.getCurrency());
        shouldHaveCategory(spend.getCategory().getName());
        shouldBeSelectedCategory(spend.getCategory().getName());
        shouldHaveDate(spend.getSpendDate());
        shouldHaveDescription(spend.getDescription());
        return (T) this;
    }

}