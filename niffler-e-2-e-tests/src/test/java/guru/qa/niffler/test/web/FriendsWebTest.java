package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;


@WebTest
public class FriendsWebTest {

  private final SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);

  @User(friends = 1)
  @Test
  void friendShouldBePresentInFriendsTable(UserJson user) {
    final String friendUsername = user.testData().friendsUsernames()[0];

    driver.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingFriends(friendUsername);
  }

  @User
  @Test
  void friendsTableShouldBeEmptyForNewUser(UserJson user) {
    driver.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingFriendsCount(0);
  }

  @User(incomeInvitations = 1)
  @Test
  void incomeInvitationBePresentInFriendsTable(UserJson user) {
    final String incomeInvitationUsername = user.testData().incomeInvitationsUsernames()[0];

    driver.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitations(incomeInvitationUsername);
  }

  @User(outcomeInvitations = 1)
  @Test
  void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
    final String outcomeInvitationUsername = user.testData().outcomeInvitationsUsernames()[0];

    driver.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toAllPeoplesPage()
        .checkInvitationSentToUser(outcomeInvitationUsername);
  }

  @User(friends = 1)
  @Test
  void shouldRemoveFriend(UserJson user) {
    final String userToRemove = user.testData().friendsUsernames()[0];

    driver.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .removeFriend(userToRemove)
        .checkExistingFriendsCount(0);
  }

  @User(incomeInvitations = 1)
  @Test
  void shouldAcceptInvitation(UserJson user) {
    final String userToAccept = user.testData().incomeInvitationsUsernames()[0];

    FriendsPage friendsPage = driver.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitationsCount(1)
        .acceptFriendInvitationFromUser(userToAccept);

    driver.refresh();

    friendsPage.checkExistingInvitationsCount(0)
        .checkExistingFriendsCount(1)
        .checkExistingFriends(userToAccept);
  }

  @User(incomeInvitations = 1)
  @Test
  void shouldDeclineInvitation(UserJson user) {
    final String userToDecline = user.testData().incomeInvitationsUsernames()[0];

    FriendsPage friendsPage = driver.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitationsCount(1)
        .declineFriendInvitationFromUser(userToDecline);

    driver.refresh();

    friendsPage.checkExistingInvitationsCount(0)
        .checkExistingFriendsCount(0);

    driver.open(PeoplePage.URL, PeoplePage.class)
        .checkExistingUser(userToDecline);
  }
}
