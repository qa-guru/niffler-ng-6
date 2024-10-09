package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;


public class JdbcTest {

    @Test
    void springJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson myself = usersDbClient.createUser(
                new UserJson(
                        null,
                        "myself",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson friend = usersDbClient.createUser(
                new UserJson(
                        null,
                        "friend",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson income = usersDbClient.createUser(
                new UserJson(
                        null,
                        "income",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson outcome = usersDbClient.createUser(
                new UserJson(
                        null,
                        "outcome",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        usersDbClient.addInvitation(income, myself);
        usersDbClient.addInvitation(myself, outcome);
        usersDbClient.addFriends(myself, friend);
    }
}