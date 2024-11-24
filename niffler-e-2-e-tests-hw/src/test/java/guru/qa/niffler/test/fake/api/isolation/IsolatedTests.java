package guru.qa.niffler.test.fake.api.isolation;

import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.api.impl.UsersApiClientImpl;
import guru.qa.niffler.service.db.UserdataDbClient;
import guru.qa.niffler.service.db.impl.jdbc.UserdataDbClientJdbc;
import guru.qa.niffler.service.db.impl.jdbc.UsersDbClientJdbc;
import guru.qa.niffler.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

@Order(0)
@Isolated
class IsolatedTests {

    private static final String USERNAME = UserUtils.generateUser().getUsername();
    private final UsersClient usersClient = new UsersApiClientImpl();
    private final UserdataDbClient userdataClient = new UserdataDbClientJdbc();

    @Order(0)
    @Test
    void shouldNotHaveUsers() {
        new UsersDbClientJdbc().removeAllUsers();
        Assertions.assertTrue(
                userdataClient.findAll()
                        .isEmpty());
    }

    @Order(1)
    @Test
    void shouldCreateNewUser() {
        usersClient.createUser(UserUtils
                .generateUser()
                .setUsername(USERNAME));

        Assertions.assertFalse(userdataClient
                .findAll()
                .isEmpty());
    }

    @Order(2)
    @Test
    void shouldHaveUser() {
        Assertions.assertFalse(
                userdataClient
                        .findByUsername(USERNAME)
                        .isEmpty());
    }

}
