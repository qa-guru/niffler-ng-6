package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import retrofit2.Response;

import java.io.IOException;

import static guru.qa.niffler.utils.OauthUtils.getCodeFromRedirectUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient {
    private static final Config CFG = Config.getInstance();

    private static final String RESPONSE_TYPE = "code";
    private static final String CLIENT_ID = "client";
    private static final String SCOPE = "openid";
    private static final String REDIRECT_URI = CFG.frontUrl() + "authorized";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String GRANT_TYPE = "authorization_code";

    private final AuthApi authApi = new RestClient.EmptyClient(CFG.authUrl(), true)
            .create(AuthApi.class);

    public void preRequest(String codeChallenge) {
        Response<Void> response;
        try {
            response = authApi.authorize(
                    RESPONSE_TYPE,
                    CLIENT_ID,
                    SCOPE,
                    REDIRECT_URI,
                    codeChallenge,
                    CODE_CHALLENGE_METHOD
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
    }

    public String login(String username, String password) {
        Response<Void> response;
        try {
            response = authApi.login(
                            username,
                            password,
                            ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"))
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.code());
        return getCodeFromRedirectUrl(response.raw().request().url().toString());
    }

    public String token(String username, String password, String code, String codeChallenge) {
        Response<JsonNode> response;
        try {
            response = authApi.token(
                            CLIENT_ID,
                            REDIRECT_URI,
                            GRANT_TYPE,
                            code,
                            codeChallenge)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(200, response.code());
        return response.body().path("id_token").asText();
    }

}
