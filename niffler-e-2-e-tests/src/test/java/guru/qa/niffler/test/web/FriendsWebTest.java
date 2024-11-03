package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.SignInPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@WebTest
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .getPageHeader()
                .clickUserAvatar()
                .clickUserMenuFriends()
                .checkThatFriendPresentInFriendsList(user.friend());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .getPageHeader()
                .clickUserAvatar()
                .clickUserMenuFriends()
                .checkThatFriendsListIsEmpty();
    }

    @Test
    void incomingInvitationBePresentInFriendsTable(@UserType(WITH_INCOMING_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .getPageHeader()
                .clickUserAvatar()
                .clickUserMenuFriends()
                .checkThatIncomingRequestPresentInFriendsList(user.incoming());
    }

    @Test
    void outgoingInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTGOING_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn(user.username(), user.password())
                .getPageHeader()
                .clickUserAvatar()
                .clickUserMenuFriends()
                .clickAllPeopleTab()
                .checkThatOutgoingRequestPresentInAllPeoplesList(user.outgoing());
    }
}