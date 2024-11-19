package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.RestClient.EmptyClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import lombok.SneakyThrows;
import retrofit2.Response;

public class AuthApiClient {

    private static final Config CFG = Config.getInstance();
    private final AuthApi authApi = new EmptyClient(CFG.authUrl(), true).create(AuthApi.class);

    /**
     * Инициализация авторизации.
     *
     * @param codeChallenge - Закодированная строка для PKCE.
     */
    @SneakyThrows
    public void preRequest(String codeChallenge) {
        Response<Void> response = authApi.authorize(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "authorized",
                codeChallenge,
                "S256"
        ).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Pre-request failed with status code: " + response.code());
        }
    }

    /**
     * Выполняет логин пользователя.
     *
     * @param username - Логин пользователя.
     * @param password - Пароль пользователя.
     * @return Authorization code.
     */
    @SneakyThrows
    public String login(String username, String password) {
        String csrfToken = ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN");

        // Выполняем запрос логина
        Response<Void> response = authApi.login(username, password, csrfToken).execute();
        // Проверяем, что запрос прошёл успешно
        if (!response.isSuccessful()) {
            throw new RuntimeException("Login failed with status code: " + response.code());
        }

        // Проверяем наличие Location в заголовках
        String locationHeader = response.raw().header("Location");
        if (locationHeader == null || !locationHeader.contains("code=")) {
            // Обрабатываем возможный редирект к финальному URL
            String finalRedirectUrl = handleRedirectToAuthorizationCode(response.raw().request().url().toString());
            if (finalRedirectUrl != null && finalRedirectUrl.contains("code=")) {
                return extractCodeFromLocation(finalRedirectUrl);
            }
            throw new RuntimeException("Authorization code not found in Location header or redirect URL");
        }

        // Извлекаем код из заголовка
        return extractCodeFromLocation(locationHeader);
    }

    /**
     * Обмен Authorization Code на токен.
     *
     * @param code         - Authorization code.
     * @param codeVerifier - Исходное значение PKCE.
     * @return ID токен (id_token).
     */
    @SneakyThrows
    public String token(String code, String codeVerifier) {
        Response<JsonNode> response = authApi.token(
                "client",
                CFG.frontUrl() + "authorized",
                "authorization_code",
                code,
                codeVerifier
        ).execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new RuntimeException("Token exchange failed with status code: " + response.code());
        }

        // Возвращаем ID токен
        return response.body().get("id_token").asText();
    }

    /**
     * Следует за редиректами и возвращает конечный URL с кодом авторизации.
     *
     * @param initialUrl - URL начального редиректа.
     * @return URL с параметром code.
     */
    @SneakyThrows
    private String handleRedirectToAuthorizationCode(String initialUrl) {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().followRedirects(true).build();
        okhttp3.Request request = new okhttp3.Request.Builder().url(initialUrl).get().build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            if (response.isRedirect() && response.header("Location") != null) {
                return handleRedirectToAuthorizationCode(response.header("Location"));
            }
            return response.request().url().toString();
        }
    }

    /**
     * Извлекает Authorization code из URL.
     *
     * @param location - URL, содержащий код авторизации.
     * @return Authorization code.
     */
    private String extractCodeFromLocation(String location) {
        String[] params = location.split("\\?");
        if (params.length < 2) {
            throw new RuntimeException("Invalid URL for code extraction: " + location);
        }

        String[] queryParams = params[1].split("&");
        for (String param : queryParams) {
            if (param.startsWith("code=")) {
                return param.substring(5);
            }
        }

        throw new RuntimeException("Authorization code not found in URL parameters");
    }
}
