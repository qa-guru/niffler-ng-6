package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement menuButton = $("button[aria-label='Menu']");
  private final SelenideElement profileLink= $("a[href='/profile']");
  private final SelenideElement statBlock = $("#stat");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }


  public ProfilePage openProfilePage(){
    menuButton.click();
    profileLink.click();
    return  new ProfilePage();
  }

  public Boolean checkStatisticBlockIsDisplayed(){
    return statBlock.isDisplayed();
  }

}
