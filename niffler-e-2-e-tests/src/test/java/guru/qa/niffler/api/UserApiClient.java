package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class UserApiClient {
    private final UserApi userApi;

    public UserApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getInstance().userdataUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.userApi = retrofit.create(UserApi.class);
    }

    public UserJson getCurrentUser(String username) throws IOException {
        Response<UserJson> response = userApi.getCurrentUser(username).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch current user.");
        }
    }

    public UserJson updateUser(UserJson user) throws IOException {
        Response<UserJson> response = userApi.updateUser(user).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to update user.");
        }
    }

    public List<UserJson> getAllUsers(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getAllUsers(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch all users.");
        }
    }

    public List<UserJson> getFriends(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getFriends(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch friends.");
        }
    }

    public UserJson sendInvitation(String username, String targetUsername) throws IOException {
        Response<UserJson> response = userApi.sendInvitation(username, targetUsername).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to send invitation.");
        }
    }

    public UserJson declineInvitation(String username, String targetUsername) throws IOException {
        Response<UserJson> response = userApi.declineInvitation(username, targetUsername).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to decline invitation.");
        }
    }

    public void removeFriend(String username, String targetUsername) throws IOException {
        Response<Void> response = userApi.removeFriend(username, targetUsername).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to remove friend.");
        }
    }
}
