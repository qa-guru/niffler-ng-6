package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @User(
            addedFriends = 1
    )
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toFriendsPage()
                .checkExistingFriends(user.testData().addedFriends());
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toFriendsPage()
                .checkNoExistingFriends();
    }

    @User(
            outcomeInvitations = 1
    )
    @Test
    void outcomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toFriendsPage()
                .checkExistingInvitations(user.testData().income());
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void incomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded()
                .getHeader()
                .toAllPeoplesPage()
                .checkInvitationSentToUser(user.testData().outcome());
    }

    @User(
            outcomeInvitations = 1
    )
    @Test
    void acceptInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .acceptFriend();
    }

    @User(
            outcomeInvitations = 1
    )
    @Test
    void declineInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .declineFriend()
                .checkNoExistingFriends();
    }


}

