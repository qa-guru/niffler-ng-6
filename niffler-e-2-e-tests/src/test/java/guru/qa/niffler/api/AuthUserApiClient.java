package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.service.RestClient;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AuthUserApiClient extends RestClient {

    protected static final Config CFG = Config.getInstance();
    private final AuthUserApi authUserApi;

    public AuthUserApiClient() {
        super(CFG.authUrl());
        this.authUserApi = retrofit.create(AuthUserApi.class);
    }


    public String registerUser(){

    }
}
