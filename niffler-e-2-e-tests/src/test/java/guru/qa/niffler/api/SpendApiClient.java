package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.apache.hc.core5.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

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
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_CREATED, response.code());
        return response.body();
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        try {
            Response<CategoryJson> response = spendApi.addCategory(category).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to create category: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating category", e);
        }
    }

    @Override
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Deleting a category is not supported by API");
    }

    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public List<SpendJson> getSpends(String username, CurrencyValues filterCurrency, String from, String to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, filterCurrency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public void deleteSpends(String username, List<String> ids) {
        Response<Void> response;
        try {
            response = spendApi.deleteSpends(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_ACCEPTED, response.code());
    }

    public CategoryJson addCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }
}
