package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.RestClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import java.io.IOException;

public class UserdataApiClient extends RestClient {

    protected static final Config CFG = Config.getInstance();

    private final UserdataApi userdataApi;

    public UserdataApiClient() {
        super(CFG.userdataUrl());
        this.userdataApi = retrofit.create(UserdataApi.class);
    }

    public UserJson getCurrentUser(String username)  {
        Response<UserJson> response;
        try{
            response=userdataApi.currentUser(username).execute();
        } catch (IOException e) {
            throw  new AssertionError(e);
        }

        Assertions.assertEquals(200, response.code());
        return response.body();
    }

}
