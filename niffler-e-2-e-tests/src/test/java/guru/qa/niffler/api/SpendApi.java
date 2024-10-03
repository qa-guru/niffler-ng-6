package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface SpendApi {

  @POST("/internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("/internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("/internal/spends/{id}")
  Call<SpendJson> addSpend(String id);

  @GET("/internal/spends/all")
  Call<SpendJson> getAllSpend();

  @DELETE("/internal/spends/remove")
  Call<SpendJson> removeSpend();

  @POST("/internal/categories/add")
  Call<CategoryJson> addCategory(CategoryJson category);

  @PATCH("/internal/categories/update")
  Call<SpendJson> editCategory();

  @GET("/internal/categories/all")
  Call<SpendJson> getAllCategories();
}