package guru.qa.niffler.test.web;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import guru.qa.niffler.jupiter.annotation.Users;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;



@ExtendWith(BrowserExtension.class)
@Users
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .goToFriendsPage()
                .checkExistingFriends(user.friend());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .goToFriendsPage()
                .checkNoExistingFriends();
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .goToFriendsPage()
                .checkExistingInvitations(user.income());
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .goToPeoplesPage()
                .checkInvitationSentToUser(user.outcome());
    }
}
