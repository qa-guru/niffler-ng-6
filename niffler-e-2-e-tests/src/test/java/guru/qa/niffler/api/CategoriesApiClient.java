package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class CategoriesApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final CategoriesApi categoriesApi = retrofit.create(CategoriesApi.class);

    public CategoryJson addCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoriesApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoriesApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        return response.body();

    }

    public List<CategoryJson> getCategories(String username, Boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = categoriesApi.getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(200, response.code());
        return response.body();
    }
}