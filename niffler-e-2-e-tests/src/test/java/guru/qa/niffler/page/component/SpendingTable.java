package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static guru.qa.niffler.condition.SpendConditions.spends;

public class SpendingTable extends BaseComponent<SpendingTable> {

  private final SearchField searchField;
  private final SelenideElement periodMenu;
  private final SelenideElement currencyMenu;
  private final ElementsCollection menuItems;
  private final SelenideElement deleteBtn;
  private final SelenideElement popup;

  private final SelenideElement tableHeader;
  private final ElementsCollection headerCells;

  private final ElementsCollection tableRows;


  public SpendingTable(SelenideDriver driver) {
    super(driver.$("#spendings"), driver);

    this.searchField = new SearchField(driver);
    this.periodMenu = self.$("#period");
    this.currencyMenu = self.$("#currency");
    this.menuItems = driver.$$(".MuiList-padding li");
    this.deleteBtn = self.$("#delete");
    this.popup = driver.$("div[role='dialog']");

    this.tableHeader = self.$(".MuiTableHead-root");
    this.headerCells = tableHeader.$$(".MuiTableCell-root");

    this.tableRows = self.$("tbody").$$("tr");
  }

  @Step("Select table period {0}")
  @Nonnull
  public SpendingTable selectPeriod(DataFilterValues period) {
    periodMenu.click();
    menuItems.find(text(period.text)).click();
    return this;
  }

  public SpendingTable checkSpendingRows(SpendJson... spendJsons) {
    tableRows.should(spends(spendJsons));
    return this;
  }

  @Step("Edit spending with description {0}")
  @Nonnull
  public EditSpendingPage editSpending(String description) {
    searchSpendingByDescription(description);
    SelenideElement row = tableRows.find(text(description));
    row.$$("td").get(5).click();
    return new EditSpendingPage(driver);
  }

  @Step("Delete spending with description {0}")
  @Nonnull
  public SpendingTable deleteSpending(String description) {
    searchSpendingByDescription(description);
    SelenideElement row = tableRows.find(text(description));
    row.$$("td").get(0).click();
    deleteBtn.click();
    popup.$(byText("Delete")).click(usingJavaScript());
    return this;
  }

  @Step("Search spending with description {0}")
  @Nonnull
  public SpendingTable searchSpendingByDescription(String description) {
    searchField.search(description);
    return this;
  }

  @Step("Check that table contains data {0}")
  @Nonnull
  public SpendingTable checkTableContains(String expectedSpend) {
    searchSpendingByDescription(expectedSpend);
    tableRows.find(text(expectedSpend)).should(visible);
    return this;
  }

  @Step("Check that table have size {0}")
  @Nonnull
  public SpendingTable checkTableSize(int expectedSize) {
    tableRows.should(size(expectedSize));
    return this;
  }
}
