package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {

  private final SelenideElement peopleTab = $("a[href='/people/friends']");
  private final SelenideElement allTab = $("a[href='/people/all']");
  private final SelenideElement requestsTable = $("#requests");
  private final SelenideElement friendsTable = $("#friends");

  @Nonnull
  public FriendsPage checkExistingFriends(String... expectedUsernames) {
    friendsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }

  @Nonnull
  public FriendsPage checkNoExistingFriends() {
    friendsTable.$$("tr").shouldHave(size(0));
    return this;
  }

  @Nonnull
  public FriendsPage checkExistingInvitations(String... expectedUsernames) {
    requestsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }
}
