package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.db.SpendDbClient;
import guru.qa.niffler.service.db.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

public class UserCreateInBdTest {

    static UserClient authUserDbClient = new UserDbClient();
    static SpendClient spendDbClient = new SpendDbClient();

    @ValueSource(strings = {
            "Ignat-110"
    })
    @ParameterizedTest
    void createUserFromRepository(String username) {

        UserJson user = authUserDbClient.createUser(
                username,
                "12345"
                );
        authUserDbClient.createIncomeInvitations(user, 1);
        authUserDbClient.createOutcomeInvitations(user, 1);
        authUserDbClient.createFriends(user, 1);
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
