package guru.qa.niffler.myapis;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson getSpend(String id, String userName){
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpend(id, userName)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }

    assertEquals(200, response.code());
    return response.body();
  }

  public List<SpendJson> getAllSpends(String userName){
    final Response<List<SpendJson>> response;
    try {
      response = spendApi.getSpends(userName)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }

    assertEquals(200, response.code());
    return response.body();
  }

  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }

    assertEquals(200, response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }

    assertEquals(200, response.code());
    return response.body();
  }

  public void removeSpends(String userName, List<String> ids) {
    final Response response;
    try {
      response = spendApi.removeSpends(userName, ids)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  public List<CategoryJson> getAllCategories(String userName, boolean excludeArchived){
    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getCategories(userName, excludeArchived)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }

    assertEquals(200, response.code());
    return response.body();
  }

  public CategoryJson addCategory(CategoryJson categoryJson){
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(categoryJson)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }

    assertEquals(200, response.code());
    return response.body();
  }

  public CategoryJson updateCategory(CategoryJson categoryJson){
    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(categoryJson)
              .execute();
    } catch (IOException e) {
      throw  new AssertionError(e);
    }

    assertEquals(200, response.code());
    return response.body();
  }
}
