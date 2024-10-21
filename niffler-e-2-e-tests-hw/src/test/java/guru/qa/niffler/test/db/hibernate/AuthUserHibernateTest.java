package guru.qa.niffler.test.db.hibernate;

import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.impl.hibernate.AuthUserDbClientHibernate;
import guru.qa.niffler.service.impl.jdbc.AuthUserDbClientJdbc;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AuthUserHibernateTest {

    private final AuthUserDbClient authUserDbClient = new AuthUserDbClientHibernate();

    @Test
    void shouldCreateNewUserTest() {
        var user = authUserDbClient.create(UserUtils.generateAuthUser());
        log.info("User: {}", user);
        assertNotNull(user.getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var user = authUserDbClient.create(UserUtils.generateAuthUser());
        var foundedUser = authUserDbClient
                .findById(user.getId())
                .orElse(new AuthUserJson());
        assertEquals(user.getId(), foundedUser.getId());
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var user = authUserDbClient.create(UserUtils.generateAuthUser());
        var foundedUser = authUserDbClient
                .findByUsername(user.getUsername())
                .orElse(new AuthUserJson());
        assertEquals(user.getId(), foundedUser.getId());
    }

    @Test
    void shouldRemoveUserTest() {
        var user = authUserDbClient.create(UserUtils.generateAuthUser());
        authUserDbClient.delete(user);
        assertNull(authUserDbClient.findByUsername(user.getUsername()).orElse(null));
    }

}
