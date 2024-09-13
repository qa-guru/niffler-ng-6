package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.apache.hc.core5.http.HttpStatus;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi
                    .getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public List<SpendJson> getSpends(String username, CurrencyValues filterCurrency, Date from, Date to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi
                    .getSpends(username, filterCurrency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public void removeSpend(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi
                    .removeSpend(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
    }

    public CategoryJson addCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi
                    .addCategory(categoryJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi
                    .updateCategory(categoryJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }

    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi
                    .getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code());
        return response.body();
    }
}
