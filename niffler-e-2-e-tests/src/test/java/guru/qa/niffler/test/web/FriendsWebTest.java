package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.SignInPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .clickHeaderUserAccountButton()
                .clickHeaderUserAccountMenuFriendsButton()
                .checkThatFriendPresentInFriendsList(user.friend());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .clickHeaderUserAccountButton()
                .clickHeaderUserAccountMenuFriendsButton()
                .checkThatFriendsListIsEmpty();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomingInvitationBePresentInFriendsTable(@UserType(WITH_INCOMING_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .clickHeaderUserAccountButton()
                .clickHeaderUserAccountMenuFriendsButton()
                .checkThatIncomingRequestPresentInFriendsList(user.incoming());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outgoingInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTGOING_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .clickHeaderUserAccountButton()
                .clickHeaderUserAccountMenuFriendsButton()
                .clickAllPeopleTab()
                .checkThatOutgoingRequestPresentInAllPeoplesList(user.outgoing());
    }
}