package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.model.CategoryJson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ParametersAreNonnullByDefault
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

    public @Nonnull CategoryJson create(CategoryJson category) {

        log.info("Create new category: {}", category);
        final Response<CategoryJson> response;
        try {
            response = categoryApi.createNewCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.CREATED, response.code());
        return Optional.ofNullable(response.body())
                .orElseThrow(()->
                        new IllegalStateException("Failed to create new category: " + response.body()));
    }

    public @Nonnull Optional<CategoryJson> findByUsernameAndName(String username, String name) {
        try {
            return Optional.of(
                    findAllByUsername(username, false).stream()
                            .filter(category -> category.getName().equals(name))
                            .toList().getFirst());
        } catch (NoSuchElementException ex) {
            return Optional.empty();
        }
    }

    public @Nonnull List<CategoryJson> findAllByUsername(String username, boolean excludeArchived) {
        log.info("Get all categories of user: [{}]", username);
        final Response<List<CategoryJson>> response;
        try {
            response = categoryApi.getAllCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public @Nonnull CategoryJson update(CategoryJson category) {
        log.info("Update category to: {}", category);
        final Response<CategoryJson> response;
        try {
            response = categoryApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return Optional.ofNullable(response.body())
                .orElseThrow(()->
                        new IllegalStateException("Failed to update category: " + response.body()));
    }

}