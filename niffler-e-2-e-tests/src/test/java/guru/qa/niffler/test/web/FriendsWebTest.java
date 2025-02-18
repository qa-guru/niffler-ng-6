package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class FriendsWebTest {

  @User(friends = 1)
  @Test
  void friendShouldBePresentInFriendsTable(UserJson user) {
    final String friendUsername = user.testData().friendsUsernames()[0];

    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingFriends(friendUsername);
  }

  @User
  @Test
  void friendsTableShouldBeEmptyForNewUser(UserJson user) {
    Selenide.open(LoginPage.URL, LoginPage.class)
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

    Selenide.open(LoginPage.URL, LoginPage.class)
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

    Selenide.open(LoginPage.URL, LoginPage.class)
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

    Selenide.open(LoginPage.URL, LoginPage.class)
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

    FriendsPage friendsPage = open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitationsCount(1)
        .acceptFriendInvitationFromUser(userToAccept)
        .checkExistingInvitationsCount(0);

    Selenide.refresh();

    friendsPage.checkExistingFriendsCount(1)
        .checkExistingFriends(userToAccept);
  }

  @User(incomeInvitations = 1)
  @Test
  void shouldDeclineInvitation(UserJson user) {
    final String userToDecline = user.testData().incomeInvitationsUsernames()[0];

    FriendsPage friendsPage = Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitationsCount(1)
        .declineFriendInvitationFromUser(userToDecline)
        .checkExistingInvitationsCount(0);

    Selenide.refresh();

    friendsPage.checkExistingFriendsCount(0);

    open(PeoplePage.URL, PeoplePage.class)
        .checkExistingUser(userToDecline);
  }
}
