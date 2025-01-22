package guru.qa.niffler.api;

import guru.qa.jaxb.userdata.AllUsersRequest;
import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.jaxb.userdata.UsersResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserdataSoapApi {

  @Headers(value = {
      "Content-type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> currentUser(@Body CurrentUserRequest currentUserRequest);

  @Headers(value = {
      "Content-type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> allUsers(@Body AllUsersRequest allUsersRequest);


}
