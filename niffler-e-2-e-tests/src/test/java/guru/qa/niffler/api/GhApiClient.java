package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.RestClient;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GhApiClient extends RestClient {

    protected static final Config CFG = Config.getInstance();
    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";
    private final GhApi ghApi;

    public GhApiClient() {
        super(CFG.ghUrl());
        this.ghApi = retrofit.create(GhApi.class);
    }

    public String issueState(String issueNumber) {
        final Response<JsonNode> response;
        try {
            response = ghApi.issue(
                            "Bearer " + System.getenv(GH_TOKEN_ENV),
                            issueNumber
                    )
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        return Objects.requireNonNull(response).body().get("state").asText();
    }
}