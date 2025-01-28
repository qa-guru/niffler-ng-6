package guru.qa.niffler.api;

import guru.qa.niffler.api.GatewayApi;

import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static guru.qa.niffler.api.CategoriesApiClient.CFG;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi = create(GatewayApi.class);
    }

    @Step("send /api/friends/all GET request to niffler-gateway")
    public List<UserJson> allFriends(@Nonnull String bearerToken,
                                     @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allFriends(bearerToken, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("api/friends/remove DELETE request to niffler-gateway")
    public void removeFriends(@Nonnull String bearerToken,
                                     @Nonnull String targetUser) {
        final Response<Void> response;
        try {
            response = gatewayApi.removeFriend(bearerToken, targetUser).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Step("api/invitations/accept POST request to niffler-gateway")
    public UserJson acceptFriends(@Nonnull String bearerToken,
                              @Nonnull FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.acceptInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }


    @Step("api/invitations/decline POST request to niffler-gateway")
    public UserJson declineInvitation(@Nonnull String bearerToken,
                                  @Nonnull FriendJson friend) {
        final Response<UserJson> response;
        try {
            response = gatewayApi.declineInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("api/users/all GET request to niffler-gateway")
    public List<UserJson> allUsers(@Nonnull String bearerToken,
                                      @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = gatewayApi.allUsers(bearerToken, searchQuery).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }
}
