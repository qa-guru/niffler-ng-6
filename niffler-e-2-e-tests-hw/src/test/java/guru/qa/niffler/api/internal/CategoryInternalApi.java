package guru.qa.niffler.api.internal;

import guru.qa.niffler.model.rest.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface CategoryInternalApi {

    @POST("internal/categories/add")
    Call<CategoryJson> createNewCategory(@Body CategoryJson category);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> getAllCategories(
            @Query("username") String username,
            @Query("excludeArchived") boolean excludeArchived
    );

    @PATCH("internal/categories/update")
    Call<CategoryJson> updateCategory(@Body CategoryJson category);

}