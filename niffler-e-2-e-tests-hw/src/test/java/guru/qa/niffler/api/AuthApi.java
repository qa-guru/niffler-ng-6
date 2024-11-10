package guru.qa.niffler.api;

import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public interface AuthApi {


    @GET("register")
    Call<Void> getCookies();

    @FormUrlEncoded
    @POST("register")
    Call<Void> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordConfirmation,
            @Field("_csrf") String csrf
    );

    @FormUrlEncoded
    @POST("login")
    Call<Void> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("_csrf") String csrf
    );

}