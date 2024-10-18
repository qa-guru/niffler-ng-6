package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.service.impl.springJdbc.AuthUserDbClientSpringJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.UserUtils.generateAuthUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class AuthUserSpringJdbcTest {

    @Test
    void shouldCreateNewUserTest() {
        Assertions.assertNotNull(
                new AuthUserDbClientSpringJdbc()
                        .create(generateAuthUser())
                        .getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var authUserDbClient = new AuthUserDbClientSpringJdbc();
        var userId = authUserDbClient
                .create(generateAuthUser())
                .getId();
        assertTrue(authUserDbClient
                .findById(userId)
                .isPresent());
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var authUserDbClient = new AuthUserDbClientSpringJdbc();
        var username = authUserDbClient
                .create(generateAuthUser())
                .getUsername();
        assertTrue(authUserDbClient
                .findByUsername(username)
                .isPresent());
    }

    @Test
    void shouldFindAll() {
        var authUserDbClient = new AuthUserDbClientSpringJdbc();
        authUserDbClient
                .create(generateAuthUser());
        assertFalse(authUserDbClient
                .findAll()
                .isEmpty());
    }

    @Test
    void shouldRemoveUserTest() {
        var authUserDbClient = new AuthUserDbClientSpringJdbc();
        var authUserJson = authUserDbClient
                .create(generateAuthUser());
        authUserDbClient.delete(authUserJson);
        assertTrue(authUserDbClient
                .findById(authUserJson.getId())
                .isEmpty());
    }

}
