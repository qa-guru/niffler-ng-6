package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.core.RestClient.EmptyClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final SpendApi spendApi = new EmptyClient(CFG.spendUrl()).create(SpendApi.class);

  @Override
  @Nonnull
  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
    return requireNonNull(response.body());
  }

  @Override
  @Nonnull
  public CategoryJson createCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(category)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    CategoryJson result = requireNonNull(response.body());

    return category.archived()
        ? updateCategory(
        new CategoryJson(
            result.id(),
            result.name(),
            result.username(),
            true
        )
    ) : result;
  }

  @Override
  @Nonnull
  public CategoryJson updateCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(category)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  public void removeCategory(CategoryJson category) {
    throw new UnsupportedOperationException("Can`t remove category using API");
  }
}
