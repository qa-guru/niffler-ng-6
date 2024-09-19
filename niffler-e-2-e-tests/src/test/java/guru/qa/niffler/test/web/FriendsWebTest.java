package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extantion.UserQueueExtension;
import guru.qa.niffler.jupiter.extantion.UserQueueExtension.StaticUser;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.jupiter.extantion.BrowserExtension;


import static guru.qa.niffler.jupiter.extantion.UserQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UserQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserQueueExtension.UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .checkHaveFriend(user.friends());
    }

    @Test
    @ExtendWith(UserQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserQueueExtension.UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .checkNotHaveFriend();
        new FriendsPage().openAllPeoplePage().checkNotHaveOutcomeInvitation();
    }

    @Test
    @ExtendWith(UserQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserQueueExtension.UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .checkIncomeInvitationFriend(user.income());
    }

    @Test
    @ExtendWith(UserQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserQueueExtension.UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openAllPeoplePage()
                .checkHaveOutcomeInvitation(user.outcome());
    }
}