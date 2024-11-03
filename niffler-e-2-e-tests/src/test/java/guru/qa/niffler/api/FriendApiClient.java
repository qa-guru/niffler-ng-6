package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.RestClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class FriendApiClient extends RestClient {

    protected static final Config CFG = Config.getInstance();
    private final FriendApi friendApi;

    public FriendApiClient() {
        super(CFG.userdataUrl());
        friendApi = retrofit.create(FriendApi.class);
    }

    public @Nullable UserJson sendInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = friendApi.sendInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        return response.body();
    }

    public @Nullable UserJson acceptInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = friendApi.acceptInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        return response.body();
    }


}
