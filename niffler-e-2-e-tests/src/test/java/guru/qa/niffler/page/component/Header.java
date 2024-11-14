package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;

public class Header extends BaseComponent<Header> {

  public Header(SelenideDriver driver) {
    super(driver.$("#root header"), driver);
    this.mainPageLink = self.$("a[href*='/main']");
    this.addSpendingBtn = self.$("a[href*='/spending']");
    this.menuBtn = self.$("button");
    this.menu = driver.$("ul[role='menu']");
    this.menuItems = menu.$$("li");
  }

  private final SelenideElement mainPageLink;
  private final SelenideElement addSpendingBtn;
  private final SelenideElement menuBtn;
  private final SelenideElement menu;
  private final ElementsCollection menuItems;

  @Step("Open Friends page")
  @Nonnull
  public FriendsPage toFriendsPage() {
    menuBtn.click();
    menuItems.find(text("Friends")).click();
    return new FriendsPage(driver);
  }

  @Step("Open All Peoples page")
  @Nonnull
  public PeoplePage toAllPeoplesPage() {
    menuBtn.click();
    menuItems.find(text("All People")).click();
    return new PeoplePage(driver);
  }

  @Step("Open Profile page")
  @Nonnull
  public ProfilePage toProfilePage() {
    menuBtn.click();
    menuItems.find(text("Profile")).click();
    return new ProfilePage(driver);
  }

  @Step("Sign out")
  @Nonnull
  public LoginPage signOut() {
    menuBtn.click();
    menuItems.find(text("Sign out")).click();
    return new LoginPage(driver);
  }

  @Step("Add new spending")
  @Nonnull
  public EditSpendingPage addSpendingPage() {
    addSpendingBtn.click();
    return new EditSpendingPage(driver);
  }

  @Step("Go to main page")
  @Nonnull
  public MainPage toMainPage() {
    mainPageLink.click();
    return new MainPage(driver);
  }
}
