package guru.qa.niffler.page.page.spending;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.component.CalendarComponent;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.page.BasePage;
import guru.qa.niffler.page.page.MainPage;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public abstract class SpendingPage<T> extends BasePage<SpendingPage<T>> {

    protected final SelenideElement title = $("form h2").as("[Page title]"),
            amountLabel = $("label[for='amount']").as("['Amount' label]"),
            amountInput = $("#amount").as("'[Amount' input]"),
            currencyLabel = $("label[for='currency']").as("['Currency' label]"),
            currencySelector = $("#currency").as("['Currency' selector]"),
            currencySelectorText = currencySelector.$x(".//span[2]").as("['Currency' selector text]"),
            currencyInput = $("input[name='currency']").as("['Currency' input]"),
            categoryLabel = $("label[for='category']").as("['Category' label]"),
            categoryInput = $("#category").as("['Category' input]"),
            descriptionInput = $("#description").as("['Description' input]"),
            descriptionLabel = $("label[for='description']").as("['Description' label]"),
            cancelButton = $("#cancel").as("['Cancel' button]"),
            saveButton = $("#save").as("['Add' button]");

    protected final ElementsCollection currenciesList = $$("#menu-currency li").as("'Currency' list"),
            categoryTagsList = $$x("//*[./label[@for='category']]//*[@role='button']").as("'Category' list");

    private final CalendarComponent calendarComponent = new CalendarComponent(
            $("div[class*=MuiDateCalendar-root]").as("Calendar form")
    );

    protected SpendingPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    public Header getHeader() {
        return new Header();
    }

    @SuppressWarnings("unchecked")
    public T fillSpendingData(SpendJson spend) {

        log.info("Fill spending data: {}", spend);

        setAmount(spend.getAmount());
        selectCurrency(spend.getCurrency());
        setCategory(spend.getCategory().getName());
        setDate(spend.getSpendDate());
        setDescription(spend.getDescription());

        return (T) this;

    }
    
    @SuppressWarnings("unchecked")
    public T fillSpendingDataWithCalendar(SpendJson spend) {

        log.info("Fill spending data: {}", spend);

        setAmount(spend.getAmount());
        selectCurrency(spend.getCurrency());
        setCategory(spend.getCategory().getName());
        selectDateFromCalendar(spend.getSpendDate());
        setDescription(spend.getDescription());

        return (T) this;

    }

    @SuppressWarnings("unchecked")
    @Step("Set amount = [{amount}]")
    T setAmount(Double amount) {
        log.info("Set amount: [{}]", amount);
        amountInput.setValue(amount.toString());
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Set category = [{category}]")
    T setCategory(String category) {
        log.info("Set category: [{}]", category);
        categoryInput.setValue(category).pressEnter();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Select category tag = [{currency}]")
    T selectCategoryFromTags(String category) {
        log.info("Select category tag: [{}]", category);
        categoryTagsList.find(exactText(category)).click();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Select currency = [{currency}]")
    T selectCurrency(CurrencyValues currency) {
        log.info("Select currency: [{}]", currency);
        currencySelector.click();
        currenciesList.find(text(currency.toString())).click();
        return (T) this;
    }

    protected abstract T selectDateFromCalendar(Date date);

    protected abstract T setDate(Date date);

    @SuppressWarnings("unchecked")
    @Step("Set description = [{description}]")
    protected  T setDescription(String description) {
        log.info("Set description: [{}]", description);
        descriptionInput.setValue(description);
        return (T) this;
    }

    @Step("Submit")
    public MainPage submit() {
        log.info("Submit spend");
        saveButton.click();
        return new MainPage(true);
    }

    @Step("Cancel")
    public MainPage cancel() {
        cancelButton.click();
        return new MainPage(true);
    }

    @SuppressWarnings("unchecked")
    @Step("Should have amount = [{amount}]")
    public T shouldHaveAmount(Double amount) {
        var amountVal = String.valueOf((amount % 1.0 == 0) ? amount.intValue() : amount);
        log.info("Spending should have amount: [{}] ", amountVal);
        amountInput.shouldHave(value(amountVal));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Should have currency = [{currency}]")
    public T shouldHaveCurrency(CurrencyValues currency) {
        log.info("Spending should have selected currency: [{}]", currency);
        currencySelectorText.shouldHave(text(currency.name()));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Should have category = [{category}]")
    public T shouldHaveCategory(String category) {
        log.info("Spending should have category: [{}]", category);
        categoryInput.shouldHave(value(category));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Should have category = [{category}]")
    public T shouldSelectedCategoryTag(String category) {
        log.info("Category tag should be selected: [{}]", category);
        categoryTagsList.find(exactText(category)).shouldHave(cssClass("MuiChip-colorPrimary"));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Should not selected category tag = [{category}]")
    public T shouldNotSelectedCategoryTag(String category) {
        log.info("Category should not be selected: [{}]", category);
        categoryTagsList.find(exactText(category)).shouldNotHave(cssClass("MuiChip-colorPrimary"));
        return (T) this;
    }

    public abstract T shouldHaveDate(Date date);

    @SuppressWarnings("unchecked")
    @Step("Should have description = [{description}]")
    public T shouldHaveDescription(String description) {
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
        descriptionInput.shouldBe(visible);
        descriptionLabel.shouldBe(visible);
        cancelButton.shouldBe(visible);
        saveButton.shouldBe(visible);
    }

    @SuppressWarnings("unchecked")
    @Step("Spending should have data:")
    public T shouldHaveData(SpendJson spend) {
        log.info("Assert spend: [{}]", spend.getUsername());
        shouldHaveAmount(spend.getAmount());
        shouldHaveCurrency(spend.getCurrency());
        shouldHaveCategory(spend.getCategory().getName());
        shouldSelectedCategoryTag(spend.getCategory().getName());
        shouldHaveDate(spend.getSpendDate());
        shouldHaveDescription(spend.getDescription());
        return (T) this;
    }

}