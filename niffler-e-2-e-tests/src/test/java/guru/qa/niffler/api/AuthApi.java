package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface AuthApi {

    @GET("oauth2/authorize")
    Call<Void> getAuthorizeCookies(
            @Query("response_type") String response_type,
            @Query("client_id") String client_id,
            @Query("scope") String scope,
            @Query(value = "redirect_uri", encoded = true) String redirect_uri,
            @Query("code_challenge") String code_challenge,
            @Query("code_challenge_method") String code_challenge_method

    );

    @POST("login")
    @FormUrlEncoded
    Call<Void> sendAuthorizeData(
            @Field("_csrf") String _csrf,
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("oauth2/token")
    Call<JsonNode> genToken(
            @Field("code") String code,
            @Field(value = "redirect_uri", encoded = true) String redirectUri,
            @Field("code_verifier") String code_verifier,
            @Field("grant_type") String grant_type,
            @Field("client_id") String client_id
    );
}
