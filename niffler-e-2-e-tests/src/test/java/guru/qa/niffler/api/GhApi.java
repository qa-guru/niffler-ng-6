package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GhApi {

  @GET("/repos/Max4life1997/niffler-ng-6/issues/{issues_number}")
  @Headers({
      "Accept: application/vnd.github+json",
      "X-GitHub-Api-Version: 2022-11-28"
  })
  Call<JsonNode> issue(@Header("Authorization") String bearerToken,
                       @Path("issues_number") String issueNumber);
}