package guru.qa.niffler.test.db.hibernate;

import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.service.impl.hibernate.UserdataDbClientHibernate;
import guru.qa.niffler.service.impl.jdbc.UserdataDbClientJdbc;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserdataHibernateTest {

    private final UserdataDbClientHibernate userdataDbClient = new UserdataDbClientHibernate();

    @Test
    void shouldCreateNewUserTest() {
        var user = userdataDbClient.create(UserUtils.generateUser());
        assertNotNull(user.getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var user = userdataDbClient.create(UserUtils.generateUser());
        log.info(user.toString());
        var foundedUser = userdataDbClient.findById(user.getId()).orElse(new UserModel());
        assertEquals(user, foundedUser);
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var user = userdataDbClient.create(UserUtils.generateUser());
        var foundedUser = userdataDbClient.findByUsername(
                        user.getUsername())
                .orElse(new UserModel());
        assertEquals(user, foundedUser);
    }

    @Test
    void shouldRemoveUserTest() {
        var user = userdataDbClient.create(UserUtils.generateUser());
        userdataDbClient.delete(user);
        assertNull(userdataDbClient.findByUsername(user.getUsername()).orElse(null));
    }

    @Test
    void shouldAddFriendTest() {
        var requester = userdataDbClient.create(UserUtils.generateUser());
        var addressee = userdataDbClient.create(UserUtils.generateUser());
        userdataDbClient.addFriend(requester, addressee);
    }

    @Test
    void shouldSendFriendRequestTest() {
        var requester = userdataDbClient.create(UserUtils.generateUser());
        var addressee = userdataDbClient.create(UserUtils.generateUser());
        userdataDbClient.createInvitation(requester, addressee);
    }

}
