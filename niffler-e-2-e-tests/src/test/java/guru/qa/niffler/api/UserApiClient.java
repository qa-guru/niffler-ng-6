package guru.qa.niffler.api;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserApiClient extends RestClient implements UsersClient {

    private final AuthApiClient authApiClient = new AuthApiClient();
    private final UserApi userApi;

    public UserApiClient() {
        super(CFG.userdataUrl());
        this.userApi = retrofit.create(UserApi.class);
    }

    @Step("Получение текущего пользователя по имени: {username}")
    public @Nullable UserJson getCurrentUser(@Nonnull String username) {
        final Response<UserJson> response;
        try {
            response = userApi.getCurrentUser(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Отправка приглашения от пользователя {username} пользователю {targetUsername}")
    public @Nullable UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        final Response<UserJson> response;
        try {
            response = userApi.sendInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Принятие приглашения от пользователя {username} пользователю {targetUsername}")
    public @Nullable UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        final Response<UserJson> response;
        try {
            response = userApi.acceptInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    @Step("Создание нового пользователя с именем: {username}")
    public @Nonnull UserJson createUser(@Nonnull String username, @Nonnull String password) {
        // Запрос формы регистрации для получения CSRF токена
        authApiClient.requestRegisterForm();
        authApiClient.registerUser(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        );

        // Ожидание появления пользователя после регистрации
        long maxWaitTime = 5000L; // 5 секунд ожидания
        Stopwatch sw = Stopwatch.createStarted();

        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            try {
                UserJson userJson = userApi.getCurrentUser(username).execute().body();
                if (userJson != null && userJson.id() != null) {
                    return userJson; // Пользователь найден, возвращаем
                } else {
                    Thread.sleep(100); // Ожидание перед следующей проверкой
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Ошибка при выполнении запроса на получение пользователя или ожидании", e);
            }
        }
        // Если пользователь не найден за отведенное время
        throw new AssertionError("Пользователь не был найден в системе после истечения времени ожидания");
    }

    @Override
    @Step("Добавление {count} входящих приглашений пользователю: {targetUser.username}")
    public @Nonnull List<String> addIncomeInvitation(@Nonnull UserJson targetUser, int count) {
        List<String> incomeUsers = new ArrayList<>();
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());
            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }
            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), "12345");
                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(newUser.username(), user.username());
                incomeUsers.add(newUser.username());
            }
        }
        return incomeUsers;
    }

    @Override
    @Step("Добавление {count} исходящих приглашений пользователю: {targetUser.username}")
    public @Nonnull List<String> addOutcomeInvitation(@Nonnull UserJson targetUser, int count) {
        List<String> outcomeUsers = new ArrayList<>();
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());
            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }
            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), "12345");
                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(user.username(), newUser.username());
                // Добавляем созданного пользователя в список
                outcomeUsers.add(newUser.username());
            }
        }
        return outcomeUsers;
    }

    @Override
    @Step("Добавление {count} друзей пользователю: {targetUser.username}")
    public @Nonnull List<String> addFriend(@Nonnull UserJson targetUser, int count) {
        List<String> friends = new ArrayList<>();
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());
            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }
            for (int i = 0; i < count; i++) {
                // Шаг 2: Отправка входящего приглашения в друзья
                List<String> incomeUsers = addIncomeInvitation(targetUser, 1);
                // Шаг 3: Принятие входящего приглашения в друзья
                acceptInvitation(user.username(), incomeUsers.get(0));
                // Добавляем созданного друга в список
                friends.add(incomeUsers.get(0));
            }
        }
        return friends;
    }
}