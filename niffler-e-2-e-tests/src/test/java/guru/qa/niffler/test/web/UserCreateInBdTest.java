package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UserClientExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.db.SpendDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;


@ExtendWith(UserClientExtension.class)
@Disabled
public class UserCreateInBdTest {

    private UserClient userClient;
    static SpendClient spendDbClient = new SpendDbClient();

    @ValueSource(strings = {
            "Ignat-110"
    })
    @ParameterizedTest
    void createUserFromRepository(String username) {

        UserJson user = userClient.createUser(
                username,
                "12345"
                );
        userClient.createIncomeInvitations(user, 1);
        userClient.createOutcomeInvitations(user, 1);
        userClient.createFriends(user, 1);
    }

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.create(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-6",
                                "esa",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx-6",
                        "esa"
                )
        );

        System.out.println(spend);
    }

}
