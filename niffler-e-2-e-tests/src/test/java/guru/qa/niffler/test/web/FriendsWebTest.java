package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extensions.BrowserExtension;
import guru.qa.niffler.jupiter.extensions.UserQueueExtension;
import guru.qa.niffler.jupiter.extensions.UserQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extensions.UserQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extensions.UserQueueExtension.UserType.Type.*;


@ExtendWith({BrowserExtension.class, UserQueueExtension.class})
public class FriendsWebTest extends BaseWebTest {

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(type = WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(user.username())
                .setPassword(user.password())
                .clickLogInButton()
                .clickAvatarButton()
                .clickFriendsButton()
                .clickShowFriendsList()
                .checkUserPresentInFriendTable(user.friend());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(type = EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(user.username())
                .setPassword(user.password())
                .clickLogInButton()
                .clickAvatarButton()
                .clickFriendsButton()
                .checkUserNotHaveFriend();
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(type = WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(user.username())
                .setPassword(user.password())
                .clickLogInButton()
                .clickAvatarButton()
                .clickFriendsButton()
                .clickShowFriendsList()
                .checkInvitationInFriendFromUserByName(user.income());
    }

    @Test
    void outcomeInvitationBePresentInAppPeoplesTable(@UserType(type = WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(user.username())
                .setPassword(user.password())
                .clickLogInButton()
                .clickAvatarButton()
                .clickFriendsButton()
                .clickShowAllPeopleList()
                .checkOutgoingFriendInvitationRequestForUserByName(user.outcome());
    }
}
