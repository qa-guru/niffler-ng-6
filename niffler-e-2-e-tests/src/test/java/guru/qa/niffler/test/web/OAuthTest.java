package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.utils.OauthUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OAuthTest {
    private final AuthApiClient authApi = new AuthApiClient();

    @User
    @Test
    void oauthTest(UserJson user) {
        // Генерация codeVerifier и codeChallenge
        String codeVerifier = OauthUtils.generateCodeVerifier();
        String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

        // Шаг 1: Предварительный запрос на авторизацию
        authApi.preRequest(codeChallenge);

        // Шаг 2: Логин пользователя, получение authorization code
        String code = authApi.login(user.username(), user.testData().password());

        // Шаг 3: Обмен authorization code на id_token
        String idToken = authApi.token(code, codeVerifier);

        // Проверка, что id_token не null
        assertNotNull(idToken, "id_token должен быть не null");
        System.out.println("idToken: " + idToken);
    }



}
