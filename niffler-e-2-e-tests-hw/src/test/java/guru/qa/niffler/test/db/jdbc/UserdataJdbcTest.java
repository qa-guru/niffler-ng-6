package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.service.impl.jdbc.UserdataDbClientJdbc;
import guru.qa.niffler.service.impl.springJdbc.UserdataDbClientSpringJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.UserUtils.generateUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class UserdataJdbcTest {

    UserdataDbClient userdataDbClient = new UserdataDbClientJdbc();

    @Test
    void shouldCreateNewUserTest() {
        Assertions.assertNotNull(userdataDbClient.create(generateUser()).getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var userId = userdataDbClient
                .create(generateUser())
                .getId();
        assertTrue(userdataDbClient
                .findById(userId)
                .isPresent());
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var username = userdataDbClient
                .create(generateUser())
                .getUsername();
        assertTrue(userdataDbClient
                .findByUsername(username)
                .isPresent());
    }

    @Test
    void shouldFindAllTest() {
        userdataDbClient
                .create(generateUser());
        assertFalse(userdataDbClient
                .findAll()
                .isEmpty());
    }

    @Test
    void shouldSendInvitationTest() {
        var requester = userdataDbClient.create(generateUser());
        var addressee = userdataDbClient.create(generateUser());
        userdataDbClient.sendInvitation(requester, addressee, FriendshipStatus.PENDING);
    }

    @Test
    void shouldAddFriendTest() {
        var requester = userdataDbClient.create(generateUser());
        var addressee = userdataDbClient.create(generateUser());
        userdataDbClient.addFriend(requester, addressee);
    }

    @Test
    void shouldRemoveUserTest() {
        var username = userdataDbClient
                .create(generateUser())
                .getUsername();
        assertTrue(userdataDbClient
                .findByUsername(username)
                .isPresent());
    }

}
