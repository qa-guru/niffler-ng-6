package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;

import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.UserClient;

import guru.qa.niffler.service.rest.UserRestClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.jupiter.extension.BrowserExtension;


import java.util.Arrays;
import java.util.List;

@Order(2)
@ExtendWith(BrowserExtension.class)
@WebTest
public class FriendsWebTest {
    static UserClient authUserClient = new UserRestClient();

    private static final Config CFG = Config.getInstance();

    @User(friends = 1)
    @ApiLogin
    @Test
    void friendShouldBePresentInFriendsTable(@Token String token, UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openFriendsPage()
                .checkHaveFriend();
    }

    @User
    @ApiLogin
    @Test
    void friendsTableShouldBeEmptyForNewUser(@Token String token, UserJson user) {
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openFriendsPage()
                .checkNotHaveFriend();
        new FriendsPage().openAllPeoplePage().checkNotHaveOutcomeInvitation();
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void incomeInvitationBePresentInFriendsTable(@Token String token, UserJson user) {
        List<String> users = Arrays.asList(user.testData().incomeInvitationsUsernames());
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .findFriend(users.get(0))
                .checkIncomeInvitationFriend();
    }

    @User(outcomeInvitations = 1)
    @ApiLogin
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@Token String token, UserJson user) {
        List<String> users = Arrays.asList(user.testData().outcomeInvitationsUsernames());
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openAllPeoplePage()
                .toSearch(users.get(0))
                .checkHaveOutcomeInvitation();

    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void acceptFriend(@Token String token, UserJson user) {
        List<String> users = Arrays.asList(user.testData().incomeInvitationsUsernames());
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openFriendsPage()
                .acceptFriendship(users.get(0))
                .checkAlert("Invitation of " + users.get(0) + " accepted");
        new FriendsPage().checkHaveFriend();
    }

    @User(incomeInvitations = 1)
    @Test
    void declineFriend(@Token String token, UserJson user) {
        List<String> users = Arrays.asList(user.testData().incomeInvitationsUsernames());
        Selenide.open(CFG.frontUrl(), MainPage.class)
                .openFriendsPage()
                .declineFriendship(users.get(0))
                .checkAlert("Invitation of " + users.get(0) + " is declined");
        new FriendsPage().checkNotHaveFriend();
    }
}