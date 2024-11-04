package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;

import guru.qa.niffler.jupiter.annotation.User;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.UserClient;

import guru.qa.niffler.service.rest.UserRestClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.jupiter.extantion.BrowserExtension;



import java.util.List;
@Order(2)
@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {
    static UserClient authUserClient = new UserRestClient();

    private static final Config CFG = Config.getInstance();

    @User
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        authUserClient.createFriends(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .checkHaveFriend();
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        authUserClient.createFriends(user, 0);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .checkNotHaveFriend();
        new FriendsPage().openAllPeoplePage().checkNotHaveOutcomeInvitation();
    }

    @User
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        List<String> users = authUserClient.createIncomeInvitations(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .findFriend(users.get(0))
                .checkIncomeInvitationFriend();
    }

    @User
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        List<String> users = authUserClient.createOutcomeInvitations(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openAllPeoplePage()
                .toSearch(users.get(0))
                .checkHaveOutcomeInvitation();

    }

    @User
    @Test
    void acceptFriend(UserJson user) {
        List<String> users = authUserClient.createIncomeInvitations(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .acceptFriendship(users.get(0))
                .checkAlert("Invitation of "+users.get(0)+" accepted");
        new FriendsPage().checkHaveFriend();
    }

    @User
    @Test
    void declineFriend(UserJson user) {
        List<String> users = authUserClient.createIncomeInvitations(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .declineFriendship(users.get(0))
                .checkAlert("Invitation of "+users.get(0)+" is declined");
        new FriendsPage().checkNotHaveFriend();
    }



}