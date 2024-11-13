package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@Order(1)
@Disabled
public class UsersNotFoundTest {
    private final UsersApiClient apiClient = new UsersApiClient();

    @User()
    @Test
    void notFindUser(UserJson user) {
        assertThat(apiClient.usersAll(user), empty());
    }
}
