package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.model.CategoryJson;
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
public class CategoryApiClientRetrofit {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(
                    new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
            )
            .build();

    private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);

    public CategoryJson create(@NonNull CategoryJson category) {

        log.info("Create new category: {}", category);
        final Response<CategoryJson> response;
        try {
            response = categoryApi.createNewCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.CREATED, response.code());
        return response.body();
    }

    public Optional<CategoryJson> findById(@NonNull UUID id) {
        throw new UnsupportedOperationException("Find category by id not supported for Api client");
    }

    public Optional<CategoryJson> findByUsernameAndName(@NonNull String username, @NonNull String name) {
        return Optional.ofNullable(
                findAllByUsername(username, false).stream()
                        .filter(category -> category.getName().equals(name))
                        .toList().getFirst());
    }

    public List<CategoryJson> findAllByUsername(@NonNull String username, boolean excludeArchived) {
        log.info("Get all categories of user: [{}]", username);
        final Response<List<CategoryJson>> response;
        try {
            response = categoryApi.getAllCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();
    }

    public CategoryJson update(@NonNull CategoryJson category) {
        log.info("Update category to: {}", category);
        final Response<CategoryJson> response;
        try {
            response = categoryApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body();
    }

}