package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserFromQueue;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.rest.StaticUser;
import guru.qa.niffler.page.page.auth.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

import static guru.qa.niffler.enums.UserType.*;

@Slf4j
@ExtendWith({UsersQueueExtension.class})
@WebTest
class PeopleQueueWebTest {

    @Test
    void shouldNotHaveFriendsTest(
            @UserFromQueue(EMPTY) StaticUser user
    ) {

        log.info("Users from queue: user = [{}]", user.getUsername());

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToFriendsPage()
                .shouldBeEmptyFriendsPage();

    }

    @Test
    void shouldBeFriendsWithEachOtherTest(
            @UserFromQueue(WITH_FRIEND) StaticUser user1,
            @UserFromQueue(WITH_FRIEND) StaticUser user2
    ) {

        log.info("Users from queue: user1 = [{}], user2 = [{}]", user1.getUsername(), user2.getUsername());

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user1.getUsername(), user1.getPassword())
                .getHeader()
                .goToFriendsPage()
                .shouldHaveFriend(user2.getUsername())

                .getHeader()
                .signOut()

                .login(user2.getUsername(), user2.getPassword())
                .getHeader()
                .goToFriendsPage()
                .shouldHaveFriend(user1.getUsername());

    }

    @Test
    void shouldHaveIncomeFriendRequestTest(
            @UserFromQueue(WITH_INCOME_REQUEST) StaticUser user
    ) {

        var usernameWhoSentRequest = user.getIncomeRequestFromUsersList().get(new Random().nextInt(user.getIncomeRequestFromUsersList().size() - 1));
        log.info("User from queue: user = [{}]", user.getUsername());

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToFriendsPage()
                .shouldHaveIncomeFriendRequest(usernameWhoSentRequest);


    }

    @Test
    void shouldHaveOutcomeFriendRequestTest(
            @UserFromQueue(WITH_OUTCOME_REQUEST) StaticUser user
    ) {

        var usernameWhoGotFriendRequest = user.getOutcomeRequestToUsersList().get(new Random().nextInt(user.getOutcomeRequestToUsersList().size() - 1));
        log.info("User from queue: user = [{}]", user.getUsername());

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .goToAllPeoplePage()
                .shouldHaveOutcomeFriendRequest(usernameWhoGotFriendRequest);

    }


}
