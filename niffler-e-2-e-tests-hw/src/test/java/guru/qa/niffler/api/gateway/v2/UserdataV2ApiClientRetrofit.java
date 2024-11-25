package guru.qa.niffler.api.gateway.v2;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.model.rest.pageable.RestResponsePage;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserdataV2ApiClientRetrofit extends RestClient {

    private final UserdataV2Api userdataApi;

    public UserdataV2ApiClientRetrofit() {
        super(CFG.gatewayUrl());
        this.userdataApi = create(UserdataV2Api.class);
    }

    @Step("send /api/friends/all GET request to niffler-gateway")
    public RestResponsePage<UserJson> allFriends(@Nonnull String bearerToken,
                                                 @Nullable String searchQuery,
                                                 int page,
                                                 @Nullable String sort) {
        final Response<RestResponsePage<UserJson>> response;
        try {
            response = userdataApi.allFriends(bearerToken, searchQuery, page, sort).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }
}
