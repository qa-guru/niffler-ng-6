package guru.qa.niffler.test.db;

import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserdataJdbcTest {

    private final UserdataDbClient userdataDbClient = new UserdataDbClient();

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
        userdataDbClient.delete(user.getId());
        assertNull(userdataDbClient.findByUsername(user.getUsername()).orElse(null));
    }

}