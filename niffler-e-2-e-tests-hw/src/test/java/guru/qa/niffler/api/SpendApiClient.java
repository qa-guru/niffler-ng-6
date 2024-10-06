package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class SpendApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(
                    new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
            )
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson createNewSpend(SpendJson spend) {

        log.info("Create new spend: {}", spend);

        final Response<SpendJson> response;
        try {
            response = spendApi.createNewSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.CREATED, response.code());
        return response.body();

    }

    public SpendJson getSpend(String id) {

        log.info("Get spend by id: {}", id);

        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();

    }

    public List<SpendJson> getSpends(
            @NonNull String username,
            @Query("filterCurrency") CurrencyValues currencyValues,
            @Query("from") Date from,
            @Query("to") Date to
    ) {

        log.info("Get spends by: username = [{}], currency = [{}], from = [{}], to = [{}]", username, currencyValues, from, to);

        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, currencyValues, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();

    }

    public SpendJson updateSpend(SpendJson spend) {

        log.info("Update spend to: {}", spend);

        final Response<SpendJson> response;
        try {
            response = spendApi.updateSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();

    }

    public void deleteSpends(String username, List<String> ids) {

        log.info("Delete spends by ids: {}", ids);

        final Response<Void> response;
        try {
            response = spendApi.deleteSpends(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.ACCEPTED, response.code());

    }

}