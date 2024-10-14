package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiClient implements UsersClient {

    private final AuthApi authApi;
    private final UsersApi usersApi;

    public UsersApiClient() {
        Retrofit authRetrofit = new Retrofit.Builder()
                .baseUrl(Config.getInstance().authUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        authApi = authRetrofit.create(AuthApi.class);

        Retrofit userdataRetrofit = new Retrofit.Builder()
                .baseUrl(Config.getInstance().userdataUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        usersApi = userdataRetrofit.create(UsersApi.class);
    }

    @Override
    public UserJson createUser(String username, String password) {
        final Response<Void> formResponse;
        try {
            formResponse = authApi.requestRegisterForm().execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при выполнении запроса формы регистрации", e);
        }

        assertEquals(200, formResponse.code());

        final String cookieHeader = formResponse.headers().get("Set-Cookie");
        if (cookieHeader == null || !cookieHeader.contains("XSRF-TOKEN")) {
            throw new AssertionError("Не удалось получить XSRF-TOKEN из Set-Cookie");
        }

        String csrfToken = null;
        for (String cookie : cookieHeader.split(";")) {
            if (cookie.contains("XSRF-TOKEN")) {
                csrfToken = cookie.split("=")[1].trim();
                break;
            }
        }

        if (csrfToken == null) {
            throw new AssertionError("XSRF-TOKEN не найден в Set-Cookie");
        }

        final Response<Void> registerResponse;
        try {
            registerResponse = authApi.register(username, password, password, csrfToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при выполнении запроса регистрации пользователя", e);
        }

        assertEquals(201, registerResponse.code(), "Ожидался код 201 при успешной регистрации");

        final Response<UserJson> userResponse;
        try {
            userResponse = usersApi.currentUser(username).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при получении данных о пользователе", e);
        }

        assertEquals(200, userResponse.code());

        UserJson user = userResponse.body();
        if (user == null || user.id() == null) {
            throw new AssertionError("Пользователь не был успешно создан, ID отсутствует");
        }

        return user;
    }

    @Override
    public void addIncomeInvitation(UserJson targetUser, int count) {
    }

    @Override
    public void addOutcomeInvitation(UserJson targetUser, int count) {
    }

    @Override
    public void addFriend(UserJson targetUser, int count) {
    }
}