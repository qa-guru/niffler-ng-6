package guru.qa.niffler.test.myweb;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.myextensions.UsersQueueExtension;
import guru.qa.niffler.mypages.FriendsPage;
import guru.qa.niffler.mypages.LoginPage;
import guru.qa.niffler.mypages.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.myextensions.UsersQueueExtension.StaticUserExtended;
import static guru.qa.niffler.jupiter.myextensions.UsersQueueExtension.UserTypeExtended;
import static guru.qa.niffler.jupiter.myextensions.UsersQueueExtension.UserTypeExtended.Type.*;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserTypeExtended(WITH_FRIENDS) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendNameInFriends(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserTypeExtended(EMPTY) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendsTableIsEmpty();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserTypeExtended(WITH_INCOME_FRIEND_REQUEST) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendIncomeRequestExistInTable(user.incomeFriendRequest());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserTypeExtended(WITH_OUTCOME_FRIEND_REQUEST) StaticUserExtended user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        FriendsPage friendsPage = mainPage.selectFriends();
        friendsPage.checkFriendsPage();
        friendsPage.selectAllPeople();
        friendsPage.checkFriendsPage();
        friendsPage.checkFriendOutcomeRequestExistInTable(user.outcomeFriendRequest());
    }
}
