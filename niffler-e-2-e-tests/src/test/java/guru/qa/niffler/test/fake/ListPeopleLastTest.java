package guru.qa.niffler.test.fake;

import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.api.parallel.ResourceLock;

@Isolated
@WebTest
public class ListPeopleLastTest {
     private UserdataApiClient userdataApiClient = new UserdataApiClient();


    @User
    @Test
    @ResourceLock("11111")
    public void listNotEmpty(UserJson user){
        Assertions.assertFalse(userdataApiClient.getAllPeople(user.username()).isEmpty());
    }

}
