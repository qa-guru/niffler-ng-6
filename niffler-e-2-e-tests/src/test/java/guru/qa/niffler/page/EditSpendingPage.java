package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.SelectField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.util.Date;

import static com.codeborne.selenide.Condition.visible;

public class EditSpendingPage extends BasePage<EditSpendingPage> {

  public static final String URL = CFG.frontUrl() + "spending";

  private final Calendar calendar;
  private final SelectField currencySelect;

  private final SelenideElement amountInput;
  private final SelenideElement categoryInput;
  private final ElementsCollection categories;
  private final SelenideElement descriptionInput;

  private final SelenideElement cancelBtn;
  private final SelenideElement saveBtn;


  public EditSpendingPage(SelenideDriver driver) {
    super(driver);
    this.calendar = new Calendar(driver);
    this.currencySelect = new SelectField(driver.$("#currency"), driver);

    this.amountInput = driver.$("#amount");
    this.categoryInput = driver.$("#category");
    this.categories = driver.$$(".MuiChip-root");
    this.descriptionInput = driver.$("#description");

    this.cancelBtn = driver.$("#cancel");
    this.saveBtn = driver.$("#save");
  }

  @Override
  @Nonnull
  public EditSpendingPage checkThatPageLoaded() {
    amountInput.should(visible);
    return this;
  }

  @Step("Fill spending data from object")
  @Nonnull
  public EditSpendingPage fillPage(SpendJson spend) {
    return setNewSpendingDate(spend.spendDate())
        .setNewSpendingAmount(spend.amount())
        .setNewSpendingCurrency(spend.currency())
        .setNewSpendingCategory(spend.category().name())
        .setNewSpendingDescription(spend.description());
  }

  @Step("Select new spending currency: {0}")
  @Nonnull
  public EditSpendingPage setNewSpendingCurrency(CurrencyValues currency) {
    currencySelect.setValue(currency.name());
    return this;
  }

  @Step("Select new spending category: {0}")
  @Nonnull
  public EditSpendingPage setNewSpendingCategory(String category) {
    categoryInput.clear();
    categoryInput.setValue(category);
    return this;
  }

  @Step("Set new spending amount: {0}")
  @Nonnull
  public EditSpendingPage setNewSpendingAmount(double amount) {
    amountInput.clear();
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Set new spending amount: {0}")
  @Nonnull
  public EditSpendingPage setNewSpendingAmount(int amount) {
    amountInput.clear();
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Set new spending date: {0}")
  @Nonnull
  public EditSpendingPage setNewSpendingDate(Date date) {
    calendar.selectDateInCalendar(date);
    return this;
  }

  @Step("Set new spending description: {0}")
  @Nonnull
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Click submit button to create new spending")
  @Nonnull
  public EditSpendingPage saveSpending() {
    saveBtn.click();
    return this;
  }
}
