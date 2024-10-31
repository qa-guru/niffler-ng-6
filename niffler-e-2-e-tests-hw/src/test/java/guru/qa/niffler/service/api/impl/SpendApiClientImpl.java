package guru.qa.niffler.service.api.impl;

import guru.qa.niffler.api.CategoryApiClientRetrofit;
import guru.qa.niffler.api.SpendApiClientRetrofit;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.api.SpendApiClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpendApiClientImpl implements SpendApiClient {

    private final SpendApiClientRetrofit spendClient = new SpendApiClientRetrofit();
    private final CategoryApiClientRetrofit categoryClient = new CategoryApiClientRetrofit();

    @NonNull
    @Override
    public SpendJson create(@NonNull SpendJson spendJson) {
        var category = spendJson.getCategory();
        spendJson.setCategory(
                categoryClient.findByUsernameAndName(spendJson.getUsername(), category.getName())
                        .orElseGet(() -> {
                            log.info("Create new category: {}", category);
                            return categoryClient.create(category);
                        })
        );
        return spendClient.create(spendJson);
    }

    @NonNull
    @Override
    public Optional<SpendJson> findById(@NonNull UUID id) {
        try {
            return Optional.of(spendClient.findById(id));
        } catch (AssertionError ex) {
            return Optional.empty();
        }
    }

    @NonNull
    @Override
    public Optional<SpendJson> findFirstSpendByUsernameAndDescription(@NonNull String username, @NonNull String description) {
        return spendClient.findFirstSpendByUsernameAndDescription(username, description);
    }

    @NonNull
    @Override
    public List<SpendJson> findAllByUsernameAndDescription(@NonNull String username, @NonNull String description) {
        return spendClient.findAllByUsernameAndDescription(username, description);
    }

    @NonNull
    @Override
    public List<SpendJson> findAllByUsername(@NonNull String username) {
        return spendClient.findAllByUsername(username);
    }

    @NonNull
    @Override
    public List<SpendJson> findAll() {
        throw new UnsupportedOperationException("Find all spends not supported for API client.");
    }

    @Override
    public SpendJson update(@NonNull SpendJson spendJson) {
        return spendClient.update(spendJson);
    }

    @Override
    public void remove(@NonNull SpendJson spendJson) {
        spendClient.remove(spendJson.getUsername(), List.of(spendJson.getId()));
    }

    @NonNull
    @Override
    public CategoryJson createCategory(@NonNull CategoryJson categoryJson) {
        return categoryClient.create(categoryJson);
    }

    @NonNull
    @Override
    public Optional<CategoryJson> findCategoryById(@NonNull UUID id) {
        try {
            return categoryClient.findById(id);
        } catch (AssertionError ex) {
            return Optional.empty();
        }
    }

    @NonNull
    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndName(@NonNull String username, @NonNull String name) {
        return categoryClient.findByUsernameAndName(username, name);
    }

    @NonNull
    @Override
    public List<CategoryJson> findAllCategoriesByUsername(@NonNull String username) {
        return categoryClient.findAllByUsername(username, false);
    }

    @NonNull
    @Override
    public List<CategoryJson> findAllCategories() {
        throw new UnsupportedOperationException("Find all categories not supported for API client");
    }

    @Override
    public CategoryJson updateCategory(@NonNull CategoryJson categoryJson) {
        return categoryClient.update(categoryJson);
    }

    @Override
    public void removeCategory(@NonNull CategoryJson categoryJson) {
        throw new UnsupportedOperationException("Remove category not supported for API client");
    }
}
