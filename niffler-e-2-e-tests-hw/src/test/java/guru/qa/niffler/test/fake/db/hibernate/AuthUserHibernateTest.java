package guru.qa.niffler.test.fake.db.hibernate;

import guru.qa.niffler.service.AuthUserClient;
import guru.qa.niffler.service.db.impl.hibernate.AuthUserDbClientHibernate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static guru.qa.niffler.utils.UserUtils.generateAuthUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class AuthUserHibernateTest {

    private final AuthUserClient authUserClient = new AuthUserDbClientHibernate();

    @Test
    void shouldCreateNewUserTest() {
        Assertions.assertNotNull(
                authUserClient
                        .create(generateAuthUser().setAuthorities(Collections.emptyList()))
                        .getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var userId = authUserClient
                .create(generateAuthUser())
                .getId();
        assertTrue(authUserClient
                .findById(userId)
                .isPresent());
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var username = authUserClient
                .create(generateAuthUser())
                .getUsername();
        assertTrue(authUserClient
                .findByUsername(username)
                .isPresent());
    }

    @Test
    void shouldFindAllTest() {
        authUserClient.create(generateAuthUser());
        assertFalse(authUserClient
                .findAll()
                .isEmpty());
    }

    @Test
    void shouldRemoveUserTest() {
        var authUserJson = authUserClient.create(generateAuthUser());
        authUserClient.remove(authUserJson);
        assertTrue(authUserClient
                .findById(authUserJson.getId())
                .isEmpty());
    }

}
