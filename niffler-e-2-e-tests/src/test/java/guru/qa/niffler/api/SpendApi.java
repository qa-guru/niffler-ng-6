package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(
          @Path("id") String id,
          @Query("username") String username
  );

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpends(
          @Query("username") String username,
          @Query("filterCurrency") CurrencyValues filterCurrency,
          @Query("from") String from,  // Date parameters should be formatted as Strings
          @Query("to") String to       // Dates will be passed as formatted Strings
  );

  @DELETE("internal/spends/remove")
  Call<SpendJson> deleteSpends(@Query("username") String username, @Query("ids") List<String> ids);

}
