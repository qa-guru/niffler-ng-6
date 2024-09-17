package guru.qa.niffler.myapis;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {


  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") String id, @Query("username") String username);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpends(@Query("username") String username);

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("/internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @DELETE("/internal/spends/remove")
  Call removeSpends(@Query("username") String username, @Query("username") List<String> ids);

  @GET("/internal/categories/all")
  Call<List<CategoryJson>> getCategories(@Query("username") String username, @Query("excludeArchived") boolean excludeArchived);

  @POST("/internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson categoryJson);

  @PATCH("/internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson categoryJson);
}
