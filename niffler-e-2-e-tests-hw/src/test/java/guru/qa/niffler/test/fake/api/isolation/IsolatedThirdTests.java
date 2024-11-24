package guru.qa.niffler.test.fake.api.isolation;

import guru.qa.niffler.service.db.impl.jdbc.UserdataDbClientJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

@Order(3)
@Isolated
class IsolatedThirdTests {

    @Test
    void shouldHaveUser() {
        Assertions.assertFalse(new UserdataDbClientJdbc()
                .findByUsername("valentin")
                .isEmpty());
    }

}
