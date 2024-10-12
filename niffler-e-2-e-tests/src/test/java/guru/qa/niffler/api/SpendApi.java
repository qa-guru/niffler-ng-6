package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

import java.util.List;

public interface SpendApi {

  //Spend
  @POST("/internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("/internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("/internal/spends/{id}")
  Call<SpendJson> addSpend(String id);

  @GET("/internal/spends/all")
  Call<List<SpendJson>> getAllSpend();

  @DELETE("/internal/spends/remove")
  Call<SpendJson> removeSpend();

  //Category
  @POST("/internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("/internal/categories/update")
  Call<CategoryJson> editCategory(@Body CategoryJson category);

  @GET("/internal/categories/all")
  Call<List<CategoryJson>> getAllCategories();
}