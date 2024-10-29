package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage extends BasePage<PeoplePage> {

  public static final String URL = CFG.frontUrl() + "people/all";

  private final SelenideElement peopleTab = $("a[href='/people/friends']");
  private final SelenideElement allTab = $("a[href='/people/all']");

  private final SearchField searchInput = new SearchField();

  private final SelenideElement peopleTable = $("#all");
  private final SelenideElement pagePrevBtn = $("#page-prev");
  private final SelenideElement pageNextBtn = $("#page-next");

  @Step("Check that the page is loaded")
  @Override
  @Nonnull
  public PeoplePage checkThatPageLoaded() {
    peopleTab.shouldBe(Condition.visible);
    allTab.shouldBe(Condition.visible);
    return this;
  }

  @Step("Send invitation to user: {username}")
  @Nonnull
  public PeoplePage sendFriendInvitationToUser(String username) {
    searchInput.search(username);
    SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
    friendRow.$(byText("Add friend")).click();
    return this;
  }

  @Step("Check invitation status for user: {username}")
  @Nonnull
  public PeoplePage checkInvitationSentToUser(String username) {
    searchInput.search(username);
    SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
    friendRow.shouldHave(text("Waiting..."));
    return this;
  }

  @Nonnull
  public PeoplePage checkExistingUser(String username) {
    searchInput.search(username);
    peopleTable.$$("tr").find(text(username)).should(visible);
    return this;
  }
}
