package guru.qa.niffler.test.soap;

import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataSoapClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@SoapTest
public class SoapUsersTest {

  private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

  @Test
  @User
  void currentUserTest(UserJson user) throws IOException {
    CurrentUserRequest request = new CurrentUserRequest();
    request.setUsername(user.username());
    UserResponse response = userdataSoapClient.currentUser(request);
    Assertions.assertEquals(
        user.username(),
        response.getUser().getUsername()
    );
  }
}
