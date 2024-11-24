package guru.qa.niffler.test.fake.api.isolation;

import guru.qa.niffler.service.db.impl.jdbc.UserdataDbClientJdbc;
import guru.qa.niffler.service.db.impl.jdbc.UsersDbClientJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

@Order(1)
@Isolated
class IsolatedFirstTests {

    @Test
    void shouldNotHaveUsers() {
        new UsersDbClientJdbc().removeAllUsers();
        Assertions.assertTrue(
                new UserdataDbClientJdbc()
                        .findAll()
                        .isEmpty());
    }

}
