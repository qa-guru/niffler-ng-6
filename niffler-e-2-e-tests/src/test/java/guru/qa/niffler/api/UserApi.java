package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserApi {
    @POST("/register")
    Call<UserJson> registerUser(@Body UserJson user);

    @GET("/internal/users/current")
    Call<UserJson> getCurrentUser(@Query("username") String username);

    @POST("/internal/users/update")
    Call<UserJson> updateUser(@Body UserJson user);

    @GET("/internal/users/all")
    Call<List<UserJson>> getAllUsers(@Query("username") String username, @Query("searchQuery") String searchQuery);

    @GET("/internal/friends/all")
    Call<List<UserJson>> getFriends(@Query("username") String username, @Query("searchQuery") String searchQuery);

    @POST("/internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("/internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @POST("/internal/invitations/decline")
    Call<UserJson> declineInvitation(@Query("username") String username, @Query("targetUsername") String targetUsername);

    @DELETE("/internal/friends/remove")
    Call<Void> removeFriend(@Query("username") String username, @Query("targetUsername") String targetUsername);
}
