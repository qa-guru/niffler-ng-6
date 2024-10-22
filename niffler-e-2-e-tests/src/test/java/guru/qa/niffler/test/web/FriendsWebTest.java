package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extantion.UserExtension;
import guru.qa.niffler.jupiter.extantion.UserQueueExtension;
import guru.qa.niffler.jupiter.extantion.UserQueueExtension.StaticUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.db.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.jupiter.extantion.BrowserExtension;
import org.junit.jupiter.api.extension.ExtensionContext;


import static guru.qa.niffler.jupiter.extantion.UserQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {
    static UserClient authUserDbClient = new UserDbClient();

    private static final Config CFG = Config.getInstance();

    @User()
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        authUserDbClient.createFriends(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .toSearch("")
                .checkHaveFriend();
    }

    @User()
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        authUserDbClient.createFriends(user, 0);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .toSearch("")
                .checkNotHaveFriend();
        new FriendsPage().openAllPeoplePage().checkNotHaveOutcomeInvitation();
    }

    @User()
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        authUserDbClient.createIncomeInvitations(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openFriendsPage()
                .toSearch("")
                .checkIncomeInvitationFriend();
    }

    @User()
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        authUserDbClient.createOutcomeInvitations(user, 1);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .openAllPeoplePage()
                .toSearch("")
                .checkHaveOutcomeInvitation();
    }
}