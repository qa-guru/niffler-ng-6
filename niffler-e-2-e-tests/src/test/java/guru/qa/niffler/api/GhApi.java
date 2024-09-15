package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GhApi {

    @GET("/repos/orkhanabbsv/niffler-ng-6/issues/{issue_number}")
    Call<JsonNode> issue(@Header("Authorization") String bearerToken,
                         @Path("issue_number") String issueNumber);
}
