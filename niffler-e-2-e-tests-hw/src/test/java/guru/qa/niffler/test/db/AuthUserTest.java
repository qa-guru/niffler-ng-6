package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.auth.AuthUserJson;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AuthUserTest {

    private final AuthUserDbClient authUserDbClient = new AuthUserDbClient();

    @Test
    void shouldCreateNewUserTest() {
        var user = authUserDbClient.create(UserUtils.generateAuthUser());
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
        var foundedUser = authUserDbClient.findByUsername(
                        user.getUsername())
                .orElse(new AuthUserJson());
        assertEquals(user.getId(), foundedUser.getId());
    }

    @Test
    void shouldRemoveUserTest() {
        var user = authUserDbClient.create(UserUtils.generateAuthUser());
        authUserDbClient.delete(user.getId());
        assertNull(authUserDbClient.findByUsername(user.getUsername()).orElse(null));
    }

}
