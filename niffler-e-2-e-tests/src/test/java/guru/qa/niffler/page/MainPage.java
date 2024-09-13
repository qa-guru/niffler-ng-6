package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.mifmif.common.regex.Main;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement menuButton = $("button[aria-label='Menu']");
  private final SelenideElement profileButton = $("a[href='/profile']");
  private final SelenideElement spendingHeader = $(".css-uxhuts");
  private final SelenideElement statisticsHeader = $(".css-giaux5");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public MainPage checkStatisticsHeader(String title) {
    statisticsHeader.shouldHave(text(title));
    return this;
  }

    public MainPage checkSpendingHeader(String title) {
        spendingHeader.shouldHave(text(title));
        return this;
    }

    public MainPage clickToMenuButton() {
        menuButton.click();
        return this;
    }

    public ProfilePage clickToProfileButton() {
        profileButton.click();
        return new ProfilePage();
    }
}
