package guru.qa.niffler.test.fake.api;

import guru.qa.niffler.service.UserdataClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.api.impl.UserdataApiClientImpl;
import guru.qa.niffler.service.api.impl.UsersApiClientImpl;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.UserUtils.generateUser;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class UsersApiTest {

    private final UsersClient usersClient = new UsersApiClientImpl();
    private final UserdataClient userdataClient = new UserdataApiClientImpl();

    @Test
    void shouldCreateNewUserInTwoDbTest() {
        var user = usersClient.createUser(UserUtils.generateUser());
        assertTrue(userdataClient.findByUsername(user.getUsername()).isPresent());
    }

    @Test
    void shouldSendIncomeInvitationsTest() {
        var requester = usersClient.createUser(generateUser());
        usersClient.getIncomeInvitationFromNewUsers(requester, 2);
    }

    @Test
    void shouldSendOutcomeInvitationsTest() {
        var requester = usersClient.createUser(generateUser());
        usersClient.sendOutcomeInvitationToNewUsers(requester, 2);
    }

    @Test
    void shouldAddNewFriendsTest() {
        var requester = usersClient.createUser(generateUser());
        usersClient.sendOutcomeInvitationToNewUsers(requester, 2);
    }

}
