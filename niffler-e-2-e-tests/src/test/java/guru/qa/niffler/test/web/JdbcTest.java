package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class JdbcTest {
    private final UserDbClient usersDbClient = new UserDbClient();

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-2",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        "duck"
                )
        );

        System.out.println(spend);
    }

    @Test
    void springJdbcTest() {
        UserJson user = usersDbClient.createUserSpringJdbcRepository(
                getUserWithName(randomUsername())
        );
        System.out.println(user);
    }

    @Test
    void springJdbcWithoutTransactionTest() {
        UserJson user = usersDbClient.createUserWithoutSpringJdbcTransaction(
                getUserWithName(randomUsername())
        );
        System.out.println(user);
    }

    @Test
    void chainedTransactionTest() {
        UserJson user = usersDbClient.createUserWithChainedTransactionManager(
                getUserWithName(randomUsername())
        );
        System.out.println(user);
    }

    @Test
    void jdbcTest() {
        UserJson user = usersDbClient.createUserJdbcTransaction(
                getUserWithName(randomUsername())
        );
        System.out.println(user);
    }

    @Test
    void jdbcWithoutTransactionTest() {
        UserJson user = usersDbClient.createUserWithoutJdbcTransaction(
                getUserWithName(randomUsername())
        );
        System.out.println(user);
    }

    @Test
    void addFriendTest() {

        UserJson myself = usersDbClient.createUserSpringJdbcRepository(
                getUserWithName("myself-4")
        );

        UserJson friend = usersDbClient.createUserSpringJdbcRepository(
                getUserWithName("myfriend-4")
        );

        UserJson income = usersDbClient.createUserSpringJdbcRepository(
               getUserWithName("income-4")
        );

        UserJson outcome = usersDbClient.createUserSpringJdbcRepository(
               getUserWithName("outcome-4")
        );

        usersDbClient.addInvitation(income, outcome);
        usersDbClient.addFriends(myself, friend);
    }

    private UserJson getUserWithName(String username) {
        return new UserJson(
                null,
                username,
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null
        );
    }
}
