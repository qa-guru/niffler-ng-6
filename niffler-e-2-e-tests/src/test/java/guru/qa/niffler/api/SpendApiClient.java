package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.PeriodValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.apache.hc.core5.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson addSpend(SpendJson spendJson) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spendJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_CREATED, response.code());
        return response.body();
    }

    public SpendJson editSpend(SpendJson spendJson) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spendJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public SpendJson getSpend(String id) {
       final Response<SpendJson> response;
       try {
           response = spendApi.getSpendById(id)
                   .execute();
       } catch (IOException e) {
           throw new AssertionError(e);
       }
       assertEquals(SC_OK, response.code());
       return response.body();
    }

    public List<SpendJson> getAllSpend(PeriodValues period, CurrencyValues currency) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getAllSpends(period, currency)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public void deleteSpends(List<String> ids) {
        Response<Void> response;
        try {
            response = spendApi.deleteSpendById(ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
    }

    public CategoryJson addCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(categoryJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(categoryJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    public List<CategoryJson> getAllCategories(boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getAllCategories(excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }
}
