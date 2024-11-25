package guru.qa.niffler.api.internal;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.model.rest.UserJson;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ParametersAreNonnullByDefault
public class UserdataInternalApiClient extends RestClient {

    private final UserdataApi userdataApi;

    public UserdataInternalApiClient() {
        super(CFG.userdataUrl());
        this.userdataApi = retrofit.create(UserdataApi.class);
    }

    public @Nullable UserJson currentUser(String username) {

        log.info("Get user by username = [{}]", username);

        final Response<UserJson> response;
        try {
            response = userdataApi.currentUser(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
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
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public @Nonnull List<UserJson> findAll(String username, String searchQuery) {
        log.info("Get all users by: username = [{}] and search query = [{}]", username, searchQuery);
        final Response<List<UserJson>> response;
        try {
            response = userdataApi.findAll(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public @Nonnull UserJson update(UserJson user) {

        log.info("Update user info to: {}", user);

        final Response<UserJson> response;
        try {
            response = userdataApi.updateUserInfo(user.getUsername(), user)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return Objects.requireNonNull(response.body());

    }

    public @Nonnull List<UserJson> friends(String username, String searchQuery) {

        log.info("Get friends of user = [{}] and friend username or full name contains = [{}]", username, searchQuery);

        final Response<List<UserJson>> response;
        try {
            response = userdataApi.friends(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();

    }

    public void removeFriend(String username, String targetUsername) {

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


    public @Nullable UserJson sendInvitation(String username, String targetUsername) {

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

    public @Nullable UserJson acceptInvitation(String username, String targetUsername) {

        log.info("Accept invitation from user [{}] to [{}]", username, targetUsername);

        final Response<UserJson> response;
        try {
            response = userdataApi.acceptInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return Objects.requireNonNull(response.body());
    }

    public @Nullable UserJson declineInvitation(String username, String targetUsername) {

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