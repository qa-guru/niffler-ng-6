package guru.qa.niffler.test.fake;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OAuthTest {

  private static final Config CFG = Config.getInstance();
  private final AuthApiClient authApiClient = new AuthApiClient();

  @Test
  @ApiLogin(username = "duck", password = "12345")
  void oauthTest(@Token String token, UserJson user) {
    System.out.println(user);
    Assertions.assertNotNull(token);
  }
}
