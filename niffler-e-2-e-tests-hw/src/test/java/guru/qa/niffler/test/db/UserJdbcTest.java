package guru.qa.niffler.test.db;

import guru.qa.niffler.data.entity.auth.AuthUserJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserJdbcTest {

    private final AuthUserDbClient authDbClient = new AuthUserDbClient();
    private final UserdataDbClient userdataDbClient = new UserdataDbClient();
    private final UserDbClient userDbClient = new UserDbClient();

    @Test
    void shouldCreateNewUserInTwoDbTest() {

        var authUser = UserUtils.generateAuthUser();
        var user = UserUtils.generateUser().setUsername(authUser.getUsername());

        userDbClient.createUserInAuthAndUserdataDBs(user);
        authUser = authDbClient.findByUsername(authUser.getUsername()).orElse(new AuthUserJson());
        user = userdataDbClient.findByUsername(authUser.getUsername()).orElse(new UserModel());

        assertEquals(authUser.getUsername(), user.getUsername());

    }

    @Test
    void shouldDeleteUserFromTwoDbTest() {

        var authUser = UserUtils.generateAuthUser();
        var user = UserUtils.generateUser().setUsername(authUser.getUsername());

        userDbClient.createUserInAuthAndUserdataDBs(user);
        log.info("Created user: {}", new AuthUserDbClient().findByUsername(user.getUsername()).toString());
        userDbClient.deleteUserFromAuthAndUserdataDBs(user);
        assertAll("User should not exists in userdata and auth dbs", () -> {
            assertTrue(authDbClient.findByUsername(authUser.getUsername()).isEmpty());
            assertTrue(userdataDbClient.findByUsername(authUser.getUsername()).isEmpty());
        });

    }

}
