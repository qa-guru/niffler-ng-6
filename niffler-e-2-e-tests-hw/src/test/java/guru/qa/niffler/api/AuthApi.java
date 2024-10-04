package guru.qa.niffler.api;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface AuthApi {


    @GET("login")
    Call<Void> getCookies();

    @FormUrlEncoded
    @POST("register")
    Call<Void> register(
            @FieldMap Map<String, String> formData,
            @Header("Cookie") String csrf,
            @Header("Cookie") String jSessionId
    );

    @FormUrlEncoded
    @POST("login")
    Call<Void> login(
            @Body Map<String, String> formData,
            @Header("Cookie") String csrf,
            @Header("Cookie") String jSessionId
    );

}
