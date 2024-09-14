package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statisticsHeader = $(".css-giaux5");
  private final SelenideElement historyHeader = $(".css-uxhuts");
  private final SelenideElement avatarImage = $("div.MuiAvatar-root");
  private final SelenideElement hrefProfile = $("a[href='/profile']");


  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }


  public MainPage shouldStatisticsHeader(String value) {
    statisticsHeader.shouldHave(text(value));
    return this;
  }

  public MainPage shouldHistoryHeader(String value) {
    historyHeader.shouldHave(text(value));
    return this;
  }

  public MainPage clickAvatar(){
    avatarImage.click();
    return this;
  }

  public ProfilePage clickProfile(){
    hrefProfile.click();
    return new ProfilePage();
  }


}
