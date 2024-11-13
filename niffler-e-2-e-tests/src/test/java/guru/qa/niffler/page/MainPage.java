package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPage extends BasePage<MainPage> {

  public static final String URL = CFG.frontUrl() + "main";

  protected final Header header;
  protected final SpendingTable spendingTable;
  protected final StatComponent statComponent;

  public MainPage(SelenideDriver driver) {
    super(driver);
    this.header = new Header(driver);
    this.spendingTable = new SpendingTable(driver);
    this.statComponent = new StatComponent(driver);
  }

  @Nonnull
  public Header getHeader() {
    return header;
  }

  @Nonnull
  public StatComponent getStatComponent() {
    statComponent.getSelf().scrollIntoView(true);
    return statComponent;
  }

  @Nonnull
  public SpendingTable getSpendingTable() {
    spendingTable.getSelf().scrollIntoView(true);
    return spendingTable;
  }

  @Nonnull
  public SpendingTable checkSpendingTable(SpendJson... spendJsons) {

    return spendingTable;
  }

  @Step("Check that page is loaded")
  @Override
  @Nonnull
  public MainPage checkThatPageLoaded() {
    header.getSelf().should(visible).shouldHave(text("Niffler"));
    statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
    spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
    return this;
  }
}
