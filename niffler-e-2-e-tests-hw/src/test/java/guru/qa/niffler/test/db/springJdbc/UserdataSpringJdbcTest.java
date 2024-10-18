package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.service.impl.jdbc.UsersDbClientJdbcXa;
import guru.qa.niffler.service.impl.springJdbc.UserdataDbClientSpringJdbc;
import guru.qa.niffler.service.impl.springJdbc.UsersDbClientSpringJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.UserUtils.generateUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class UserdataSpringJdbcTest {

    @Test
    void shouldCreateNewUserTest() {
        Assertions.assertNotNull(
                new UserdataDbClientSpringJdbc()
                        .create(generateUser())
                        .getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var authUserDbClient = new UserdataDbClientSpringJdbc();
        var userId = authUserDbClient
                .create(generateUser())
                .getId();
        assertTrue(authUserDbClient
                .findById(userId)
                .isPresent());
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var authUserDbClient = new UserdataDbClientSpringJdbc();
        var username = authUserDbClient
                .create(generateUser())
                .getUsername();
        assertTrue(authUserDbClient
                .findByUsername(username)
                .isPresent());
    }

    @Test
    void shouldFindAll() {
        var authUserDbClient = new UserdataDbClientSpringJdbc();
        authUserDbClient
                .create(generateUser());
        assertFalse(authUserDbClient
                .findAll()
                .isEmpty());
    }

    @Test
    void shouldRemoveUserTest() {
        var authUserDbClient = new UserdataDbClientSpringJdbc();
        var username = authUserDbClient
                .create(generateUser())
                .getUsername();
        assertTrue(authUserDbClient
                .findByUsername(username)
                .isPresent());
    }

}
