package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.impl.springJdbc.AuthUserDbClientSpringJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.UserUtils.generateAuthUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class AuthUserSpringJdbcTest {
    
    private final AuthUserDbClient authUserDbClient = new AuthUserDbClientSpringJdbc();
    
    @Test
    void shouldCreateNewUserTest() {
        Assertions.assertNotNull(
                authUserDbClient
                        .create(generateAuthUser())
                        .getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var userId = authUserDbClient
                .create(generateAuthUser())
                .getId();
        assertTrue(authUserDbClient
                .findById(userId)
                .isPresent());
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var username = authUserDbClient
                .create(generateAuthUser())
                .getUsername();
        assertTrue(authUserDbClient
                .findByUsername(username)
                .isPresent());
    }

    @Test
    void shouldFindAllTest() {
        authUserDbClient
                .create(generateAuthUser());
        assertFalse(authUserDbClient
                .findAll()
                .isEmpty());
    }

    @Test
    void shouldRemoveUserTest() {
        var authUserJson = authUserDbClient
                .create(generateAuthUser());
        authUserDbClient.remove(authUserJson);
        assertTrue(authUserDbClient
                .findById(authUserJson.getId())
                .isEmpty());
    }

}
