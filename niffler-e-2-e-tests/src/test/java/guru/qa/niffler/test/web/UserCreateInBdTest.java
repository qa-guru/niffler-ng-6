package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class UserCreateInBdTest {
    @Test
    void createUserInJdbc() {
        UserJson user = new AuthUserDbClient().createUserJdbs(new UserJson(
                null,
                "Ignat-jdbc",
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }

    @Test
    void createUserInJdbcTx() {
        UserJson user = new AuthUserDbClient().createUserJdbsTx(new UserJson(
                null,
                "Ignat-jdbcTx",
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }

    @Test
    void createUserSpringInDb() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        UserJson user = authUserDbClient.createUserSpring(new UserJson(
                null,
                "Ignat-spring",
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }

    @Test
    void createUserSpringInDbTxXa() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        UserJson user = authUserDbClient.createUserSpringTxXa(new UserJson(
                null,
                "Ignat-springTx1",
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }

    @Test
    void createUserSpringCtmInDbTx() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        UserJson user = authUserDbClient.createUserJdbsCtmTx(new UserJson(
                null,
                "Ignat-springTxCtm3",
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }

    @Test
    void createUserSpringInDbTx() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        UserJson user = authUserDbClient.createUserJdbsSpringTx(new UserJson(
                null,
                "Ignat-springTxCtm2",
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }

}
