package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();


    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(UserType.Type.WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.clickFriendButton();
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.shouldFriendItem("dima");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(UserType.Type.EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.clickFriendButton();
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.shouldEmptyFriendList("There are no users yet");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(UserType.Type.WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.clickFriendButton();
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.shouldFriendName(user.income());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(UserType.Type.WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password());
        MainPage mainPage = new MainPage();
        mainPage.clickAllPeopleButton();
        FriendsPage friendsPage = new FriendsPage();
        friendsPage.shouldFriendName(user.outcome());
    }


}

