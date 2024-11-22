package guru.qa.niffler.test.fake.db.hibernate;

import guru.qa.niffler.service.UserdataClient;
import guru.qa.niffler.service.db.impl.hibernate.UserdataDbClientHibernate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.UserUtils.generateUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class UserdataHibernateTest {

    UserdataClient userdataClient = new UserdataDbClientHibernate();

    @Test
    void shouldCreateNewUserTest() {
        var authUser = userdataClient.create(
                generateUser());
        Assertions.assertNotNull(authUser.getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        Assertions.assertNotNull(
                userdataClient
                        .create(generateUser())
                        .getId());
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var username = userdataClient
                .create(generateUser())
                .getUsername();
        assertTrue(userdataClient
                .findByUsername(username)
                .isPresent());
    }

    @Test
    void shouldFindAllTest() {
        userdataClient
                .create(generateUser());
        assertFalse(userdataClient
                .findAll()
                .isEmpty());
    }

    @Test
    void shouldSendInvitationTest() {
        var requester = userdataClient.create(generateUser());
        var addressee = userdataClient.create(generateUser());
        userdataClient.sendInvitation(requester, addressee);
    }

    @Test
    void shouldAddFriendTest() {
        var requester = userdataClient.create(generateUser());
        var addressee = userdataClient.create(generateUser());
        userdataClient.addFriend(requester, addressee);
    }

    @Test
    void shouldRemoveUserTest() {
        var username = userdataClient
                .create(generateUser())
                .getUsername();
        assertTrue(userdataClient
                .findByUsername(username)
                .isPresent());
    }

}
