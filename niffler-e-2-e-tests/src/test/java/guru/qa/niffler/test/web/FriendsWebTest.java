package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extantion.UserFriensQueueExtension;
import guru.qa.niffler.jupiter.extantion.UserFriensQueueExtension.StaticUser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import guru.qa.niffler.jupiter.extantion.BrowserExtension;


import static guru.qa.niffler.jupiter.extantion.UserFriensQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UserFriensQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserFriensQueueExtension.UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .
        ;
    }

    @Test
    @ExtendWith(UserFriensQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserFriensQueueExtension.UserType(EMPTY) StaticUser user) {}

    @Test
    @ExtendWith(UserFriensQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserFriensQueueExtension.UserType(WITH_INCOME_REQUEST) StaticUser user) {}

    @Test
    @ExtendWith(UserFriensQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserFriensQueueExtension.UserType(WITH_OUTCOME_REQUEST) StaticUser user) {}

}
