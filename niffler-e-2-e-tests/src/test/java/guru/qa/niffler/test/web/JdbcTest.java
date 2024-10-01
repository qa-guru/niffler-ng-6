package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserdataDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name",
                                "duck1",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        "duck1"
                )
        );
        System.out.println(spend);
    }

    @Test
    void createAuthUserTest() {
        UserdataDbClient userdataDbClient = new UserdataDbClient();

        UserJson authUser = userdataDbClient.createUser(
                new AuthUserJson(
                        null,
                        "pork21",
                        "12345",
                        true,
                        true,
                        true,
                        true
                ));
        System.out.println(authUser);
    }
}
