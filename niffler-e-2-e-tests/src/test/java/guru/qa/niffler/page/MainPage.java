package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends GlobalTemplatePage {

  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statsModule = $("#stat");
  private final SelenideElement statsModuleTitle = statsModule.$("h2");
  private final SelenideElement statsModuleCanvas = statsModule.$("canvas");
  private final SelenideElement statsModuleLegendContainer = statsModule.$("#legend-container");
  private final SelenideElement spendingModule = $("#spendings");
  private final SelenideElement spendingModuleTitle = spendingModule.$("h2");
  private final SelenideElement spendingModuleSearchInput = spendingModule.$("input[aria-label='search']");
  private final SelenideElement spendingModuleSearchButton = spendingModule.$("button#input-submit");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public MainPage checkStatisticsModuleDisplayed() {
    // Assert the statistics module is visible
    statsModule.shouldBe(Condition.visible);
    statsModuleTitle.shouldHave(Condition.text("Statistics"));
    statsModuleCanvas.shouldBe(Condition.visible);
    statsModuleLegendContainer.shouldBe(Condition.visible);
    return this;
  }

  public MainPage checkSpendingModuleDisplayed() {
    spendingModule.shouldBe(Condition.visible);
    spendingModuleTitle.shouldHave(Condition.text("History of Spendings"));
    spendingModuleSearchInput.shouldBe(Condition.visible).shouldBe(Condition.enabled);
    spendingModuleSearchButton.shouldBe(Condition.visible).shouldBe(Condition.enabled);
    return this;
  }

}
