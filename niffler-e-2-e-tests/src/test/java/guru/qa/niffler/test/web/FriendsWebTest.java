package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtensions;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtensions.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtensions.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest extends BaseTest {

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    @DisplayName("Friend should be present in friends table")
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIENDS) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password());

        mainPage.openFriendsPage();
        friendsPage.checkThatFriendPresentInFriendsTable(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    @DisplayName("Friend table should be empty for new user")
    void friendTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {

    }

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    void incomeInvatationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {

    }

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    void outcomeInvatationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {

    }
}
