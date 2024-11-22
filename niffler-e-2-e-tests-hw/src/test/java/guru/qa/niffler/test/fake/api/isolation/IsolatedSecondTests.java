package guru.qa.niffler.test.fake.api.isolation;

import guru.qa.niffler.service.api.impl.UsersApiClientImpl;
import guru.qa.niffler.service.db.impl.jdbc.UserdataDbClientJdbc;
import guru.qa.niffler.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

@Order(2)
@Isolated
class IsolatedSecondTests {

    @Test
    void shouldCreateNewUser() {
        new UsersApiClientImpl().createUser(UserUtils.generateUser().setUsername("valentin"));
        Assertions.assertFalse(new UserdataDbClientJdbc().findAll().isEmpty());
    }

}
