package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UsersApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

public class JdbcTest {


    @Test
    void createUserTest() {
        // Инициализируем API-клиент
        UsersApiClient usersApiClient = new UsersApiClient();
        // Создаем пользователя с использованием API-клиента
        String username = "testuser1234";
        String password = "12345";
        UserJson createdUser = usersApiClient.createUser(username, password);
        // Вывод результата в консоль, если нужно для отладки (опционально)
        System.out.println("Создан пользователь: " + createdUser);
    }

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-3",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx-3",
                        "duck"
                )
        );

        System.out.println(spend);
    }


    static UsersDbClient usersDbClient = new UsersDbClient();

    @ValueSource(strings = {
            "valentin-10"
    })
    @ParameterizedTest
    void springJdbcTest(String uname) {

        UserJson user = usersDbClient.createUser(
                uname,
                "12345"
        );

        usersDbClient.addIncomeInvitation(user, 1);
        usersDbClient.addOutcomeInvitation(user, 1);
    }
}