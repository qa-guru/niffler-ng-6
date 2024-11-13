package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Order(Integer.MAX_VALUE)
@Disabled
public class UsersFoundTest {
    private final UsersApiClient apiClient = new UsersApiClient();

    @User()
    @Test
    void findUser(UserJson user) {
        List<UserJson> userJsons = apiClient.usersAll(user);
        assertFalse(userJsons.isEmpty());
    }
}
