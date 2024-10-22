package guru.qa.niffler.test.db.hibernate;

import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.impl.hibernate.AuthUserDbClientHibernate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static guru.qa.niffler.utils.UserUtils.generateAuthUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class AuthUserHibernateTest {
    
    private final AuthUserDbClient authUserDbClient = new AuthUserDbClientHibernate();
    
    @Test
    void shouldCreateNewUserTest() {
        Assertions.assertNotNull(
                authUserDbClient
                        .create(generateAuthUser().setAuthorities(Collections.emptyList()))
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
        authUserDbClient.create(generateAuthUser());
        assertFalse(authUserDbClient
                .findAll()
                .isEmpty());
    }

    @Test
    void shouldRemoveUserTest() {
        var authUserJson = authUserDbClient.create(generateAuthUser());
        authUserDbClient.remove(authUserJson);
        assertTrue(authUserDbClient
                .findById(authUserJson.getId())
                .isEmpty());
    }

}
