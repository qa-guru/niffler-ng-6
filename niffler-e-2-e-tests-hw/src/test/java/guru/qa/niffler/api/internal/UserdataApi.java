package guru.qa.niffler.api.internal;

import guru.qa.niffler.model.rest.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UserdataApi {

    @GET("internal/users/current")
    Call<UserJson> currentUser(@Query("username") String username);

    @GET("internal/users/all")
    Call<List<UserJson>> findAll(@Query("username") String username);

    @GET("internal/users/all")
    Call<List<UserJson>> findAll(@Query("username") String username,
                                 @Query("searchQuery") String searchQuery);

    @POST("internal/users/update")
    Call<UserJson> updateUserInfo(@Query("username") String username,
                                  @Body UserJson user);

    @GET("internal/friends/update")
    Call<List<UserJson>> friends(@Query("username") String username,
                                 @Query("searchQuery") String searchQuery);

    @DELETE("internal/friends/update")
    Call<Void> removeFriend(@Query("username") String username,
                            @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username,
                                  @Query("targetUsername") String searchQuery);

    @POST("internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username,
                                    @Query("targetUsername") String searchQuery);

    @POST("internal/invitations/decline")
    Call<UserJson> declineInvitation(@Query("username") String username,
                                     @Query("targetUsername") String searchQuery);

}