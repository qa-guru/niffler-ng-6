package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.helper.EnumHelper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubApiClientRetrofit {

    private static final String GITHUB_TOKEN = System.getenv("GITHUB_TOKEN");
    private static final String GITHUB_TOKEN_NAME = System.getenv("GITHUB_TOKEN_NAME") != null
            ? System.getenv("GITHUB_TOKEN_NAME")
            : "Niffler";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().gitHubUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(
                    new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
            )
            .build();

    private final GitHubApi gitHubApi = retrofit.create(GitHubApi.class);

    public IssueState getIssueState(String issueId) {
        final Response<JsonNode> response;
        try {
            response = gitHubApi.getIssue(
                            GITHUB_TOKEN_NAME,
                            "Bearer " + GITHUB_TOKEN,
                            issueId
                    )
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());

        return EnumHelper.getEnumByNameIgnoreCase(
                IssueState.class
                , Objects.requireNonNull(response.body()).get("state").asText()
        );
    }

    public enum IssueState {
        OPEN, CLOSED
    }

}
