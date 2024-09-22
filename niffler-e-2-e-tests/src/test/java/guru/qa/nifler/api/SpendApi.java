package guru.qa.nifler.api;

import guru.qa.nifler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendApi {

  @POST("/internal/spends/add")
  Call<SpendJson> createSpend(@Body SpendJson spend);
}