package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UsersQueueExtension.UserType(WITH_FRIEND) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        new MainPage().clickOnFriendButtonUnderPersonIcon()
                .clickOnFriendButton()
                .checkIfFriendNameIsVisible(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UsersQueueExtension.UserType(EMPTY) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        new MainPage().clickOnFriendButtonUnderPersonIcon()
                .clickOnFriendButton()
                .checkIfNoFriendsInPanel("There are no users yet");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UsersQueueExtension.UserType(WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        new MainPage().clickOnFriendButtonUnderPersonIcon()
                .clickOnFriendButton()
                .checkIfFriendNameIsVisibleInRequestsList(user.income());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UsersQueueExtension.UserType(WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        new MainPage().clickOnFriendButtonUnderPersonIcon()
                .clickOnAllPeopleButton()
                .checkIfOutcomeFriendRequestIsVisibleInAllList(user.outcome());
    }
}
