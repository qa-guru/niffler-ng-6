package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;
import static guru.qa.niffler.page.BaseTest.CFG;

@WebTest
public class FriendsWebTest {

    @Test
    @DisplayName("У пользователя есть друг")
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .existingFriendsCheck(user.friend());
    }

    @Test
    @DisplayName("У нового пользователя нет друзей")
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .noExistingFriendsCheck();
    }

    @Test
    @DisplayName("У пользователя есть входящая заявка в друзья")
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .invitationsToFriendsCheck(user.income());
    }

    @Test
    @DisplayName("У пользователя есть отправленная заявка в друзья")
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .openFriendsPage()
                .clickAllPeopleBtn()
                .invitationSentToUserCheck(user.outcome());
    }
}
