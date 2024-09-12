package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  private final SelenideElement spendingHistoryTable = $("#spendings");
  private final SelenideElement spendingStatisticsCanvas = $("#stat");

  public void isSpendingStatisticsDisplayed() {
    spendingStatisticsCanvas.shouldBe(visible);
  }

  public MainPage isSpendingHistoryTableDisplayed() {
    spendingHistoryTable.shouldBe(visible);

    return this;
  }

  private final SelenideElement accountMenuBtn = $("button[aria-label='Menu']");
  private final SelenideElement profileLink = $("a[href='/profile']");

  public ProfilePage openProfile() {
    accountMenuBtn.click();
    profileLink.click();
    return new ProfilePage();
  }
}
