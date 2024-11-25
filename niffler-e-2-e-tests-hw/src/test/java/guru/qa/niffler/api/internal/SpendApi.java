package guru.qa.niffler.api.internal;

import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;

@ParametersAreNonnullByDefault
public interface SpendApi {

    @POST("internal/spends/add")
    Call<SpendJson> createNewSpend(@Body SpendJson spend);

    @GET("internal/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") String id);

    @GET("internal/spends/all")
    Call<List<SpendJson>> getSpends(
            @Query("username") String username,
            @Nullable @Query("filterCurrency") CurrencyValues currencyValues,
            @Nullable @Query("from") Date from,
            @Nullable @Query("to") Date to
    );

    @PATCH("internal/spends/edit")
    Call<SpendJson> updateSpend(@Body SpendJson spend);

    @DELETE("internal/spends/remove")
    Call<Void> deleteSpends(
            @Query("username") String username,
            @Query("ids") List<String> ids
    );

}