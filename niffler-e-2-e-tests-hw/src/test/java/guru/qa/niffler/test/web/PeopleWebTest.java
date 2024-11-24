package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.people.FriendsPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@WebTest
class PeopleWebTest {

    @Test
    void shouldAcceptIncomeInvitationTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    incomeInvitations = 1
            )
            UserJson user
    ) {
        var requester = user.getTestData().getIncomeInvitations().getFirst();
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .acceptFriendRequest(requester.getUsername())
                .shouldNotHaveIncomeFriendRequest(requester.getUsername())
                .shouldHaveFriend(requester.getUsername());
    }

    @Test
    void shouldDeclineIncomeInvitation(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    incomeInvitations = 1
            )
            UserJson user
    ) {
        var requester = user.getTestData().getIncomeInvitations().getFirst();
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .declineFriendRequest(requester.getUsername())
                .shouldNotHaveIncomeFriendRequest(requester.getUsername())
                .switchToAllPeopleTab()
                .shouldHaveStatusNonFriend(requester.getUsername());
    }

}
