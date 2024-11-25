package guru.qa.niffler.api.internal;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.enums.HttpStatus;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ParametersAreNonnullByDefault
public class SpendApiInternalClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiInternalClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public @Nonnull SpendJson create(SpendJson spend) {

        log.info("Create new spend: {}", spend);

        final Response<SpendJson> response;
        try {
            response = spendApi.createNewSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.CREATED, response.code());
        return Optional.ofNullable(response.body())
                .orElseThrow(() ->
                        new IllegalStateException("Failed to create new spend: " + response.body()));

    }

    public @Nullable SpendJson findById(UUID id) {

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

    public @Nonnull Optional<SpendJson> findFirstSpendByUsernameAndDescription(String username, String description) {
        return findAllByUsernameAndDescription(username, description).stream().findFirst();
    }

    public @Nonnull List<SpendJson> findAllByUsernameAndDescription(String username, String description) {
        return findAllByUsername(username).stream().filter(spend -> spend.getDescription().equals(description)).toList();
    }

    public @Nonnull List<SpendJson> findAllByUsername(String username) {
        log.info("Get all spends by: username = [{}]", "a");
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, null, null, null)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public @Nonnull List<SpendJson> findAllByUsername(String username,
                                                      @Nullable CurrencyValues currency,
                                                      @Nullable Date from,
                                                      @Nullable Date to) {
        log.info("Get all spends by: username = [{}]", "a");
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, currency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public @Nonnull SpendJson update(SpendJson spend) {

        log.info("Update spend to: {}", spend);

        final Response<SpendJson> response;
        try {
            response = spendApi.updateSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.OK, response.code());
        return Optional.ofNullable(response.body())
                .orElseThrow(() -> new IllegalStateException("Failed to update spend: " + response.body()));

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