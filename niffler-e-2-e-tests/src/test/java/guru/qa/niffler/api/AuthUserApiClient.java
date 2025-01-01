package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.ThreadSafeCookieStore;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class AuthUserApiClient extends RestClient {

    protected static final Config CFG = Config.getInstance();
    private final AuthUserApi authUserApi;

    public AuthUserApiClient() {
        super(CFG.authUrl());
        this.authUserApi = retrofit.create(AuthUserApi.class);
    }

    @Step("Запрос формы регистрации для получения CSRF токена")
    public void requestRegisterForm() {
        final Response<Void> response;
        try {
            response = authUserApi.getRegisterPage().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertTrue(200 == response.code());
    }

    @Step("Регистрация нового пользователя c именем: {username}")
    public void registerUser(String username, String password) {
        final Response<Void> response;
        try {
            response = authUserApi.register(
                    username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertTrue(201 == response.code());

    }
}
