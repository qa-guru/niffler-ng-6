package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserCreateInBdTest {
    @Test
    void createUserInJdbc() {
        UserJson user = new AuthUserDbClient().createUserJdbc(new UserJson(
                null,
                RandomDataUtils.randomUsername(),
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
        UserJson user = new AuthUserDbClient().createUserJdbcTx(new UserJson(
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
        UserJson user = authUserDbClient.createUserJdbcCtmTx(new UserJson(
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
        UserJson user = authUserDbClient.createUserJdbcSpringTx(new UserJson(
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

    @Test
    void createUserFromRepository() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        UserJson user = authUserDbClient.createUserRepository(new UserJson(
                null,
                "Ignat-createUserRepository1",
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
    void createFriendship() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        authUserDbClient.createUsersFriendShipJdbc(new UserJson(
                        null,
                        "friend3",
                        null,
                        null,
                        RandomDataUtils.randomName(),
                        CurrencyValues.RUB,
                        null,
                        null
                ),
                new UserJson(
                        null,
                        "friend4",
                        null,
                        null,
                        RandomDataUtils.randomName(),
                        CurrencyValues.RUB,
                        null,
                        null
                ), FriendshipStatus.ACCEPTED
        );

    }

}
