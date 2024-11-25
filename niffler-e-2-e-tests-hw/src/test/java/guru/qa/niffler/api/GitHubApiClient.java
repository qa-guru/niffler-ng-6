package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.ex.UnknownIssueStatusException;
import guru.qa.niffler.helper.EnumHelper;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GitHubApiClient extends RestClient {

    private static final String GITHUB_TOKEN = Objects.requireNonNull(System.getenv("GITHUB_TOKEN"));
    private static final String GITHUB_TOKEN_NAME = System.getenv("GITHUB_TOKEN_NAME") != null
            ? System.getenv("GITHUB_TOKEN_NAME")
            : "Niffler";

    private final GitHubApi gitHubApi;

    public GitHubApiClient() {
        super(CFG.gitHubUrl());
        this.gitHubApi = retrofit.create(GitHubApi.class);
    }

    public @Nonnull IssueState getIssueState(String issueId) {
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

        var issueStatus = Objects.requireNonNull(response.body()).get("state").asText();
        return Optional.ofNullable(
                        EnumHelper.getEnumByNameIgnoreCase(
                                IssueState.class
                                , issueStatus
                        ))
                .orElseThrow(() -> new UnknownIssueStatusException("Unknown issue status = [" + issueStatus + "]"));
    }

    public enum IssueState {
        OPEN, CLOSED
    }

}
