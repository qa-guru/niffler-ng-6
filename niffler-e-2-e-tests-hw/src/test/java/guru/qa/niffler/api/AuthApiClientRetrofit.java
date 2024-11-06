package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.enums.Token;
import guru.qa.niffler.model.UserJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@ParametersAreNonnullByDefault
public class AuthApiClientRetrofit extends RestClient {

    private final AuthApi authApi;
    private final UserdataApiClientRetrofit userdataApiClient = new UserdataApiClientRetrofit();

    public AuthApiClientRetrofit() {
        super(CFG.authUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

    public @Nonnull UserJson register(String username, String password) {
        log.info("Register user: username = [{}], password = [{}]", username, password);
        final Response<Void> response;
        try {
            authApi.getCookies().execute();
            response = authApi.register(
                            username,
                            password,
                            password,
                            ThreadSafeCookieStore.INSTANCE.cookieValue(Token.CSRF.getCookieName()))
                    .execute();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
        Assertions.assertEquals(HttpStatus.CREATED, response.code());

        StopWatch sw = StopWatch.createStarted();
        UserJson user = null;

        while (user == null && sw.getTime(TimeUnit.SECONDS) < 30) {
            try {
                user = userdataApiClient.currentUser(username);
                if (user != null && user.getId() != null) {
                    break;
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(user.setPassword(password)).orElseThrow(
                () -> new RuntimeException("Could not find user = [" + username + "]"));
    }

    public @Nonnull UserJson login(UserJson user) {

        log.info("Login by: username = [{}], password = [{}]", user.getUsername(), user.getPassword());
        final Response<Void> response;
        try {
            authApi.getCookies().execute();
            response = authApi.login(
                            user.getUsername(),
                            user.getPassword(),
                            ThreadSafeCookieStore.INSTANCE.cookieValue(Token.CSRF.getCookieName()))
                    .execute();
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }

        Assertions.assertEquals(HttpStatus.OK, response.code());

        return user;

    }


}