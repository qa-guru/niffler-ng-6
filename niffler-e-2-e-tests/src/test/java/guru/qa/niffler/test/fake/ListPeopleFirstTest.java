package guru.qa.niffler.test.fake;

import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

@Order(1)
@Isolated
@ExtendWith(BrowserExtension.class)
public class ListPeopleFirstTest {
     private UserdataApiClient userdataApiClient = new UserdataApiClient();

    @User
    @Test
    void listIsEmpty(UserJson user){
        Assertions.assertTrue(userdataApiClient.getAllPeople(user.username()).isEmpty());
    }
}
