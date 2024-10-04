package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import jaxb.userdata.FriendState;
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

    // Тест на создание пользователя без дружбы (friendState null)
    @Test
    void createUserWithoutFriend() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithFriend(
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println("Created user without friend: " + user);
    }

    // Тест на создание пользователя с VOID состоянием дружбы
    @Test
    void createUserWithVoidFriendState() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithFriend(
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        FriendState.VOID
                )
        );

        // Печатаем данные пользователя в консоль для проверки
        System.out.println("Created user with VOID friend state: " + user);
    }

    // Тест на создание пользователя с отправленным приглашением (friendState INVITE_SENT)
    @Test
    void createUserWithInviteSent() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithFriend(
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        FriendState.INVITE_SENT
                )
        );
        System.out.println("Created user with invite sent: " + user);
    }

    // Тест на создание пользователя с полученным приглашением (friendState INVITE_RECEIVED)
    @Test
    void createUserWithInviteReceived() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithFriend(
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        FriendState.INVITE_RECEIVED
                )
        );
        System.out.println("Created user with invite received: " + user);
    }

    // Тест на создание пользователя с подтвержденной дружбой (friendState FRIEND)
    @Test
    void createUserWithFriendConfirmed() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithFriend(
                new UserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        FriendState.FRIEND
                )
        );

        // Печатаем данные пользователя в консоль для проверки
        System.out.println("Created user with confirmed friend: " + user);
    }

    // Имитируем ошибку при создании пользователя, передавая неверные данные
    @Test
    void createUserTransactionRollbackOnError() {
            UsersDbClient usersDbClient = new UsersDbClient();
            UserJson user = usersDbClient.createUserWithFriend(
                    new UserJson(
                            null,
                            null, // Отсутствует имя пользователя, что вызовет ошибку
                            null,
                            null,
                            null,
                            CurrencyValues.RUB,
                            null,
                            null,
                            FriendState.FRIEND
                    )
            );
        // Печатаем данные пользователя в консоль для проверки
        System.out.println("Created user with invalid username: " + user);
    }
}