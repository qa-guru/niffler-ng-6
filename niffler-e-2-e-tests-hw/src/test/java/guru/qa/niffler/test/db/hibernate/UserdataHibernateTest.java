package guru.qa.niffler.test.db.hibernate;

import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.service.impl.hibernate.UserdataDbClientHibernate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.UserUtils.generateUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class UserdataHibernateTest {

    UserdataDbClient userdataDbClient = new UserdataDbClientHibernate();

    @Test
    void shouldCreateNewUserTest() {
        var authUser = userdataDbClient.create(
                generateUser());
        Assertions.assertNotNull(authUser.getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        Assertions.assertNotNull(
                userdataDbClient
                        .create(generateUser())
                        .getId());
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
    void shouldSendIncomeInvitationsTest() {
        var requester = userdataDbClient.create(generateUser());
        userdataDbClient.getIncomeInvitationFromNewUsers(requester, 2);
    }

    @Test
    void shouldSendOutcomeInvitationsTest() {
        var requester = userdataDbClient.create(generateUser());
        userdataDbClient.sendOutcomeInvitationToNewUsers(requester, 2);
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
