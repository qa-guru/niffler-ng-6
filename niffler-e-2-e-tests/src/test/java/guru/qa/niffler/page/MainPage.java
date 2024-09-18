package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

  private final SelenideElement header = $("#root header");
  private final SelenideElement headerMenu = $("ul[role='menu']");
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statComponent = $("#stat");
  private final SelenideElement spendingTable = $("#spendings");

  public FriendsPage friendsPage() {
    header.$("button").click();
    headerMenu.$$("li").find(text("Friends")).click();
    return new FriendsPage();
  }

  public PeoplePage allPeoplesPage() {
    header.$("button").click();
    headerMenu.$$("li").find(text("All People")).click();
    return new PeoplePage();
  }

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

  private final SelenideElement personIcon = $("[data-testid='PersonIcon']");
  private final SelenideElement friendsLink = $("a[href='/people/friends']");
  private final SelenideElement allPeopleLink = $("a[href='/people/all']");

  public FriendsPage openFriends() {
    personIcon.click();
    friendsLink.click();
    return new FriendsPage();
  }

  public FriendsPage openAllPeople() {
    personIcon.click();
    allPeopleLink.click();
    return new FriendsPage();
  }
  
  public MainPage checkThatPageLoaded() {
    statComponent.should(visible).shouldHave(text("Statistics"));
    spendingTable.should(visible).shouldHave(text("History of Spendings"));
    return this;
  }
}
