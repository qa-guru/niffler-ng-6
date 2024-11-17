package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.CreateNewUserExtension;
import guru.qa.niffler.jupiter.extension.FriendshipExtension;
import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.auth.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith({
        CreateNewUserExtension.class,
        FriendshipExtension.class,
        CategoryExtension.class,
        SpendingExtension.class
})
@WebTest
class PeopleWebTest {

    private static final String LOGIN_PAGE_URL = Config.getInstance().authUrl();

    @Test
    void shouldAcceptIncomeInvitationTest(
            @CreateNewUser(
                    incomeInvitations = 1
            )
            UserJson user
    ) {
        var requester = user.getTestData().getIncomeInvitations().getFirst();
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToFriendsPage()
                .acceptFriendRequest(requester.getUsername())
                .shouldNotHaveIncomeFriendRequest(requester.getUsername())
                .shouldHaveFriend(requester.getUsername());
    }

    @Test
    void shouldDeclineIncomeInvitation(
            @CreateNewUser(
                    incomeInvitations = 1
            )
            UserJson user
    ) {
        var requester = user.getTestData().getIncomeInvitations().getFirst();
        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToFriendsPage()
                .declineFriendRequest(requester.getUsername())
                .shouldNotHaveIncomeFriendRequest(requester.getUsername())
                .switchToAllPeopleTab()
                .shouldHaveStatusNonFriend(requester.getUsername());
    }

}
