package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GatewayApiClient extends RestClient {

  private final GatewayApi gatewayApi;

  public GatewayApiClient() {
    super(CFG.gatewayUrl());
    this.gatewayApi = create(GatewayApi.class);
  }

  @Step("send /api/friends/all GET request to niffler-gateway")
  public List<UserJson> allFriends(@Nonnull String bearerToken,
                                   @Nullable String searchQuery) {
    final Response<List<UserJson>> response;
    try {
      response = gatewayApi.allFriends(bearerToken, searchQuery).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }
}
