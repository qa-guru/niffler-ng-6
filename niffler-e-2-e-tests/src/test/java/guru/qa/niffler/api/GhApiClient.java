package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.config.Config;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Objects;

public class GhApiClient {

    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().ghUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final GhApi ghApi = retrofit.create(GhApi.class);

    public String issueState(String issueNumber) {
        String token = System.getenv(GH_TOKEN_ENV);
        if (token == null) {
            throw new IllegalStateException("GITHUB_TOKEN environment variable is not set");
        }
        try {
            JsonNode responseBody = ghApi.issue(
                    "Bearer " + token,
                    issueNumber
            ).execute().body();
            return Objects.requireNonNull(responseBody).get("state").asText();
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch issue state", e);
        }
    }
}
