package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
public class EmptyUsersTest {
    @User
    @Test
    void emptyUsersListTest(UserJson user) {
        UsersApiClient usersApiClient = new UsersApiClient();
        List<UserJson> response = usersApiClient.allUsers(user.username(), null);
        assertTrue(response.isEmpty(), "Список пользователей должен быть пустым");
    }
}
