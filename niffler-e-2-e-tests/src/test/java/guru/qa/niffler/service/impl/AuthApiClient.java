package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.CodeInterceptor;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.utils.OAuthUtils;
import lombok.SneakyThrows;
import retrofit2.Response;


public class AuthApiClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final AuthApi authApi;

  public AuthApiClient() {
    super(CFG.authUrl(), true, new CodeInterceptor());
    this.authApi = create(AuthApi.class);
  }

  @SneakyThrows
  public String login(String username, String password) {
    final String codeVerifier = OAuthUtils.generateCodeVerifier();
    final String codeChallenge = OAuthUtils.generateCodeChallange(codeVerifier);
    final String redirectUri = CFG.frontUrl() + "authorized";
    final String clientId = "client";

    authApi.authorize(
        "code",
        clientId,
        "openid",
        redirectUri,
        codeChallenge,
        "S256"
    ).execute();

    authApi.login(
        username,
        password,
        ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
    ).execute();

    Response<JsonNode> tokenResponse = authApi.token(
        ApiLoginExtension.getCode(),
        redirectUri,
        clientId,
        codeVerifier,
        "authorization_code"
    ).execute();

    return tokenResponse.body().get("id_token").asText();
  }
}
