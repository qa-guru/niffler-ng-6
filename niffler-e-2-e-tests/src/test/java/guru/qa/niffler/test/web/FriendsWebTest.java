package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();
    MainPage mainPage = new MainPage();
    FriendsPage friendsPage = new FriendsPage();

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(UserType.Type.WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.clickFriendButton();
        friendsPage.shouldFriendItem("dima");
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(UserType.Type.EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.clickFriendButton();
        friendsPage.shouldEmptyFriendList("There are no users yet");
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(UserType.Type.WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.clickFriendButton();
        friendsPage.shouldFriendName(user.income());
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(UserType.Type.WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.clickAllPeopleButton();
        friendsPage.shouldFriendRequestListVisible(user.outcome());
    }


}

