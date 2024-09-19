package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(type = WITH_FRIEND) StaticUser user) {
        FriendsPage friendsPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .goToFriends();

        friendsPage
                .checkNameInFriendList(user.friend())
                .myFriendsHeaderShouldBeVisible()
                .friendRequestsHeaderShouldNotBeVisible();
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(type = EMPTY) StaticUser user) {
        FriendsPage friendsPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password()).goToFriends();

        friendsPage
                .friendListShouldBeEmpty()
                .myFriendsHeaderShouldNotBeVisible()
                .friendRequestsHeaderShouldNotBeVisible();
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(type = WITH_INCOME_REQUEST) StaticUser user) {
        FriendsPage friendsPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password()).goToFriends();

        friendsPage
                .checkNameInRequestList(user.income())
                .myFriendsHeaderShouldBeVisible()
                .friendRequestsHeaderShouldBeVisible();
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(type = WITH_OUTCOME_REQUEST) StaticUser user) {
        FriendsPage friendsPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .goToFriends();

        friendsPage
                .clickAllPeople()
                .checkNameInAllPeopleList(user.outcome())
                .checkOutcomeInvitationInAllPeopleList(user.outcome());
    }
}
