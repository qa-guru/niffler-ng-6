package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);


  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
              .execute();
    }catch (IOException e){
      throw new AssertionError(e);
    }
    Assertions.assertEquals(201, response.code());
    return response.body();
  }


  public SpendJson updateSpend(SpendJson spend)  {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend)
              .execute();
    }catch (IOException e){
        throw new AssertionError(e);
    }
    Assertions.assertEquals(200,response.code());
    return response.body();
  }



  public List<SpendJson> getSpends(String username, CurrencyValues filterCurrency, Date from, Date to)  {
    final Response<List<SpendJson>> response;
    try {
      response=spendApi.getSpends(username, filterCurrency, from, to)
              .execute();
    }catch (IOException e){
      throw new AssertionError(e);
    }
    Assertions.assertEquals(200,response.code());
    return response.body();
  }


  public SpendJson getSpend(String id, String username)  {
    final Response<SpendJson> response;
    try {
      response=spendApi.getSpend(id, username)
              .execute();
    }catch (IOException e){
      throw new AssertionError(e);
    }
    Assertions.assertEquals(200,response.code());
    return response.body();
  }

  public void deleteSpends(String username, List<String> ids)  {
    final Response<Void> response;
    try {
      response=spendApi.deleteSpends(username, ids)
              .execute();
    }catch (IOException e){
      throw new AssertionError(e);
    }
    Assertions.assertEquals(200,response.code());

  }




}
