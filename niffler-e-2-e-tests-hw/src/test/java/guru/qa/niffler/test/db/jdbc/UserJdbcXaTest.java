package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.data.entity.auth.AuthUserJson;
import guru.qa.niffler.service.AuthAuthorityDbClient;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.service.impl.jdbc.*;
import guru.qa.niffler.service.impl.springJdbc.*;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserJdbcXaTest {

    private final UsersDbClient usersDbClient = new UsersDbClientJdbcXa();
    private final AuthAuthorityDbClient authorityDbClient = new AuthAuthorityDbClientJdbc();;
    private final AuthUserDbClient authUserDbClient = new AuthUserDbClientJdbc();
    private final UserdataDbClient userdataDbClient = new UserdataDbClientJdbc();

    @Test
    void shouldCreateNewUserInTwoDbTest() {

        var user = usersDbClient.createUserInAuthAndUserdataDBs(UserUtils.generateUser());

        var authUser = authUserDbClient.findByUsername(user.getUsername());
        var authorities = authorityDbClient.findByUserId(authUser.orElse(new AuthUserJson()).getId());
        var userdataUser = userdataDbClient.findByUsername(user.getUsername());

        assertAll("Users from niffler-auth and niffler-userdata should exists and have authorities", () -> {
            assertEquals(2, authorities.size());
            assertTrue(authUser.isPresent());
            assertTrue(userdataUser.isPresent());
        });

    }

    @Test
    void shouldDeleteUserFromTwoDbTest() {

        var user = usersDbClient.createUserInAuthAndUserdataDBs(UserUtils.generateUser());
        var authUser = authUserDbClient.findByUsername(user.getUsername()).orElse(new AuthUserJson());
        usersDbClient.deleteUserFromAuthAndUserdataDBs(user);

        var authorities = authorityDbClient.findByUserId(authUser.getId());
        var authUserAfterDelete = authUserDbClient.findByUsername(user.getUsername());
        var userdataUser = userdataDbClient.findByUsername(user.getUsername());

        assertAll("User should not exists in niffler-auth and niffler-userdata", () -> {
            assertTrue(authUserAfterDelete.isEmpty());
            assertTrue(authorities.isEmpty());
            assertTrue(userdataUser.isEmpty());
        });

    }

}
