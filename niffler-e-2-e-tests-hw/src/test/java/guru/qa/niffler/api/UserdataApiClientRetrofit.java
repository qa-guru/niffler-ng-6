package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.model.UserJson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class UserdataApiClientRetrofit {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .followRedirects(false)
                    .build())
            .build();

    private final UserdataApi userdataApi = retrofit.create(UserdataApi.class);

    public UserJson currentUser(@Nonnull String username) {

        log.info("Get user by username = [{}]", username);

        final Response<UserJson> response;
        try {
            response = userdataApi.currentUser(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.CREATED, response.code());
        return response.body();

    }


    public @Nonnull List<UserJson> findAll() {
        log.info("Get all users");
        final Response<List<UserJson>> response;
        try {
            response = userdataApi.findAll("")
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();
    }

    public @Nonnull List<UserJson> findAll(@Nonnull String username, @Nonnull String searchQuery) {
        log.info("Get all users by: username = [{}] and search query = [{}]", username, searchQuery);
        final Response<List<UserJson>> response;
        try {
            response = userdataApi.findAll(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();
    }

    public UserJson update(@Nonnull UserJson user) {

        log.info("Update user info to: {}", user);

        final Response<UserJson> response;
        try {
            response = userdataApi.updateUserInfo(user.getUsername(), user)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();

    }

    public List<UserJson> friends(@Nonnull String username, String searchQuery) {

        log.info("Get friends of user = [{}] and friend username or full name contains = [{}]", username, searchQuery);

        final Response<List<UserJson>> response;
        try {
            response = userdataApi.friends(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();

    }

    public void removeFriend(@Nonnull String username, @Nonnull String targetUsername) {

        log.info("Unfriend users [{}] and [{}]", username, targetUsername);

        final Response<Void> response;
        try {
            response = userdataApi.removeFriend(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());

    }


    public UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {

        log.info("Send invitation from user [{}] to [{}]", username, targetUsername);

        final Response<UserJson> response;
        try {
            response = userdataApi.sendInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();

    }

    public UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {

        log.info("Accept invitation from user [{}] to [{}]", username, targetUsername);

        final Response<UserJson> response;
        try {
            response = userdataApi.acceptInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();
    }

    public UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) {

        log.info("Decline invitation from user [{}] to [{}]", username, targetUsername);

        final Response<UserJson> response;
        try {
            response = userdataApi.declineInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();
    }

}