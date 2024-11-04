package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.api.core.RestClient.EmptyClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UsersApiClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final String defaultPassword = "12345";

  private final AuthApi authApi = new EmptyClient(CFG.authUrl()).create(AuthApi.class);
  private final UserdataApi userdataApi = new EmptyClient(CFG.userdataUrl()).create(UserdataApi.class);

  @NotNull
  @Override
  @Step("Crete user using API")
  public UserJson createUser(String username, String password) {
    try {
      authApi.requestRegisterForm().execute();
      authApi.register(
          username,
          password,
          password,
          ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
      ).execute();
      UserJson createdUser = requireNonNull(userdataApi.currentUser(username).execute().body());
      return createdUser.addTestData(
          new TestData(
              password
          )
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addIncomeInvitation(UserJson targetUser, int count) {
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        final String username = randomUsername();
        final Response<UserJson> response;
        final UserJson newUser;
        try {
          newUser = createUser(username, defaultPassword);

          response = userdataApi.sendInvitation(
              newUser.username(),
              targetUser.username()
          ).execute();
        } catch (IOException e) {
          throw new AssertionError(e);
        }
        assertEquals(200, response.code());

        targetUser.testData()
            .incomeInvitations()
            .add(newUser);
      }
    }
  }

  @Override
  public void addOutcomeInvitation(UserJson targetUser, int count) {
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        final String username = randomUsername();
        final Response<UserJson> response;
        final UserJson newUser;
        try {
          newUser = createUser(username, defaultPassword);

          response = userdataApi.sendInvitation(
              targetUser.username(),
              newUser.username()
          ).execute();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        assertEquals(200, response.code());

        targetUser.testData()
            .outcomeInvitations()
            .add(newUser);
      }
    }
  }

  @Override
  public void addFriend(UserJson targetUser, int count) {
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        final String username = randomUsername();
        final Response<UserJson> response;
        try {
          userdataApi.sendInvitation(
              createUser(
                  username,
                  defaultPassword
              ).username(),
              targetUser.username()
          ).execute();
          response = userdataApi.acceptInvitation(targetUser.username(), username).execute();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        assertEquals(200, response.code());

        targetUser.testData()
            .friends()
            .add(response.body());
      }
    }
  }
}
