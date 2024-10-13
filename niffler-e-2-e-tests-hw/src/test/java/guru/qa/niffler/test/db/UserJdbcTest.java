package guru.qa.niffler.test.db;

import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserJdbcTest {

    private final UserDbClient userDbClient = new UserDbClient();

    @Test
    void shouldCreateNewUserTest() {
        var user = userDbClient.create(UserUtils.generateValidUser());
        assertNotNull(user.getId());
    }

    @Test
    void shouldGetUserByIdTest() {
        var user = userDbClient.create(UserUtils.generateValidUser());
        var foundedUser = userDbClient.findById(user.getId()).orElse(new UserModel());
        assertEquals(user, foundedUser);
    }

    @Test
    void shouldGetUserByUsernameTest() {
        var user = userDbClient.create(UserUtils.generateValidUser());
        var foundedUser = userDbClient.findByUsername(
                        user.getUsername())
                .orElse(new UserModel());
        assertEquals(user, foundedUser);
    }

    @Test
    void shouldRemoveUserTest() {
        var user = userDbClient.create(UserUtils.generateValidUser());
        userDbClient.delete(user.getId());
        assertNull(userDbClient.findByUsername(user.getUsername()).orElse(null));
    }

}
