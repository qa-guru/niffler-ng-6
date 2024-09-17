package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
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
            response =
                    spendApi.addSpend(spend)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.CREATED_201, response.code());
        return response.body();
    }

    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;

        try {
            response =
                    spendApi.editSpend(spend)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    public SpendJson getSpend(String id, String userName) {
        final Response<SpendJson> response;

        try {
            response =
                    spendApi.getSpend(id, userName)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    public List<SpendJson> getAllSpends(String userName, CurrencyValues currencyValues,
                                        String from, String to) {
        final Response<List<SpendJson>> response;

        try {
            response =
                    spendApi.getSpends(userName, currencyValues, from, to)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    public SpendJson removeSpend(String userName, List<String> ids) {
        final Response<SpendJson> response;

        try {
            response =
                    spendApi.deleteSpends(userName, ids)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;

        try {
            response =
                    spendApi.addCategory(category)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;

        try {
            response =
                    spendApi.updateCategory(category)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;

        try {
            response =
                    spendApi.getCategories(username, excludeArchived)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }
}