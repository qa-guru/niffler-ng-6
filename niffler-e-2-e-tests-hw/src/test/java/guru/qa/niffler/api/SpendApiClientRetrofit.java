package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class SpendApiClientRetrofit {

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

    public SpendJson create(SpendJson spend) {

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

    public SpendJson findById(@NonNull UUID id) {

        log.info("Get spend by id: {}", id);

        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id.toString())
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();

    }

    public @NonNull Optional<SpendJson> findFirstSpendByUsernameAndDescription(String username, String description) {
        return findAllByUsernameAndDescription(username, description).stream().findFirst();
    }

    public @NonNull List<SpendJson> findAllByUsernameAndDescription(@NonNull String username, @NonNull String description) {
        return findAllByUsername(username).stream().filter(spend -> spend.getDescription().equals(description)).toList();
    }

    public @NonNull List<SpendJson> findAllByUsername(@NonNull String username) {
        log.info("Get all spends by: username = [{}]", "a");
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, null, null, null)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();
    }

    public SpendJson update(SpendJson spend) {

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

    public void remove(String username, List<UUID> ids) {

        log.info("Delete spends by ids: {}", ids);

        final Response<Void> response;
        try {
            response = spendApi.deleteSpends(
                            username,
                            ids.stream().map(UUID::toString).toList())
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.ACCEPTED, response.code());

    }
}