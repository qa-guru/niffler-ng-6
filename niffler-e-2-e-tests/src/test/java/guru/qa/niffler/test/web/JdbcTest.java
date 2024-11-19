package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UsersClientExtension;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.utils.OauthUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(UsersClientExtension.class)
public class JdbcTest {

  private UsersClient usersClient;

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            1000.0,
            CurrencyValues.RUB,
            new CategoryJson(
                null,
                "cat-name-tx-3",
                "duck",
                false
            ),
            "spend-name-tx-3",
            "duck"
        )
    );
  }

  @ValueSource(strings = {
      "valentin-11"
  })
  @ParameterizedTest
  void springJdbcTest(String uname) {
    UserJson user = usersClient.createUser(
        uname,
        "12345"
    );

    usersClient.addIncomeInvitation(user, 1);
    usersClient.addOutcomeInvitation(user, 1);
  }

  @Test
  void getToken() {
    String username = "duck";
    String password = "12345";
    AuthApiClient authApiClient = new AuthApiClient();

    String codeVerifier = OauthUtils.generateCodeVerifier();
    String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);


    authApiClient.preRequest(codeChallenge);
    String code =  authApiClient.login(username, password);
    String token = authApiClient.token(username, password, code, codeVerifier);
    assertNotNull(token);
  }
}
