package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.api.core.interceptor.AuthorizedCodeInterceptor;
import guru.qa.niffler.api.core.store.AuthStore;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.enums.CookieType;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.OAuthUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.enums.CookieType.CSRF;

@Slf4j
@ParametersAreNonnullByDefault
public class AuthApiClientRetrofit extends RestClient {

    private static final String REDIRECT_URI = CFG.frontUrl() + "authorized",
            RESPONSE_TYPE = "code",
            CLIENT_ID = "client",
            SCOPE = "openid",
            CODE_CHALLENGE_METHOD = "S256",
            GRANT_TYPE = "authorization_code";

    private final AuthApi authApi;
    private final UserdataApiClientRetrofit userdataApiClient = new UserdataApiClientRetrofit();

    public AuthApiClientRetrofit() {
        super(CFG.authUrl(),
                true,
                HttpLoggingInterceptor.Level.BODY,
                new AuthorizedCodeInterceptor());
        this.authApi = retrofit.create(AuthApi.class);
    }

    public @Nonnull UserJson register(String username, String password) {

        setUserAndClearCookieStore(username);

        log.info("Register user: username = [{}], password = [{}]", username, password);

        final Response<Void> response;
        try {
            authApi.getCookies().execute();
            response = authApi.register(
                            username,
                            password,
                            password,
                            ThreadSafeCookieStore.INSTANCE.cookieValue(CookieType.CSRF.getCookieName()))
                    .execute();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }

        Assertions.assertEquals(HttpStatus.CREATED, response.code());
        UserJson user = waitUserFromUserdata(username);

        return Optional.ofNullable(user.setPassword(password)).orElseThrow(
                () -> new RuntimeException("Could not find user = [" + username + "]"));
    }

    @Nonnull
    public String signIn(String username, String password) {

        setUserAndClearCookieStore(username);

        final String codeVerifier = OAuthUtils.generateCodeVerifier();

        log.info("Sign in user by: username = [{}], password = [{}]", username, password);

        authorize(OAuthUtils.generateCodeChallenge(codeVerifier));
        login(username, password);
        return token(codeVerifier);
    }

    private void authorize(final String codeChallenge) {
        final Response<Void> response;
        try {
            response = authApi.authorize(
                    RESPONSE_TYPE,
                    CLIENT_ID,
                    SCOPE,
                    REDIRECT_URI,
                    codeChallenge,
                    CODE_CHALLENGE_METHOD
            ).execute();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        Assertions.assertEquals(HttpStatus.OK, response.code());
    }

    private void login(String username, String password) {
        final Response<Void> response;
        try {
            response = authApi.login(
                            username,
                            password,
                            ThreadSafeCookieStore.INSTANCE.cookieValue(CSRF.getCookieName()))
                    .execute();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        Assertions.assertEquals(HttpStatus.OK, response.code());
    }

    @Nonnull
    private String token(String codeVerifier) {
        final Response<JsonNode> response;
        try {
            var code = Objects.requireNonNull(AuthStore.INSTANCE.getCurrentUserCode());
            response = authApi.token(
                            CLIENT_ID,
                            REDIRECT_URI,
                            GRANT_TYPE,
                            code,
                            codeVerifier)
                    .execute();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        Assertions.assertEquals(HttpStatus.OK, response.code());

        var token = Objects.requireNonNull(response.body())
                .get("id_token")
                .asText();

        AuthStore.INSTANCE.setCurrentUserToken(token);
        return token;
    }

    @Nullable
    private UserJson waitUserFromUserdata(String username) {
        StopWatch sw = StopWatch.createStarted();
        UserJson user = null;

        while (user == null && sw.getTime(TimeUnit.SECONDS) < 30) {
            try {
                user = userdataApiClient.currentUser(username);
                if (user != null && user.getId() != null) {
                    break;
                } else {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    private static void setUserAndClearCookieStore(String username) {
        AuthStore.INSTANCE.setCurrentUser(username);
        ThreadSafeCookieStore.INSTANCE.removeAll();
    }

}