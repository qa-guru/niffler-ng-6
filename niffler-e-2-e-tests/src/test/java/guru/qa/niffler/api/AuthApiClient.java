package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

    @Step("Запрос формы регистрации для получения CSRF токена")
    public void requestRegisterForm() {
        final Response<Void> response;
        try {
            response = authApi.requestRegisterForm().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Step("Регистрация нового пользователя c именем: {username}")
    public void registerUser(@Nonnull String username, @Nonnull String password, @Nonnull String passwordSubmit, @Nonnull String _csrf) {
        final Response<Void> response;
        try {
            response = authApi.register(username, password, passwordSubmit, _csrf).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }
}