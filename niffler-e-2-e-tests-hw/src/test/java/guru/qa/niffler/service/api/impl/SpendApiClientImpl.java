package guru.qa.niffler.service.api.impl;

import guru.qa.niffler.api.CategoryApiClientRetrofit;
import guru.qa.niffler.api.SpendApiClientRetrofit;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.api.SpendApiClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpendApiClientImpl implements SpendApiClient {

    private final SpendApiClientRetrofit spendClient = new SpendApiClientRetrofit();
    private final CategoryApiClientRetrofit categoryClient = new CategoryApiClientRetrofit();

    @Nonnull
    @Override
    public SpendJson create(@Nonnull SpendJson spendJson) {
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

    @Nonnull
    @Override
    public Optional<SpendJson> findById(@Nonnull UUID id) {
        try {
            return Optional.of(spendClient.findById(id));
        } catch (AssertionError ex) {
            return Optional.empty();
        }
    }

    @Nonnull
    @Override
    public Optional<SpendJson> findFirstSpendByUsernameAndDescription(@Nonnull String username, @Nonnull String description) {
        return spendClient.findFirstSpendByUsernameAndDescription(username, description);
    }

    @Nonnull
    @Override
    public List<SpendJson> findAllByUsernameAndDescription(@Nonnull String username, @Nonnull String description) {
        return spendClient.findAllByUsernameAndDescription(username, description);
    }

    @Nonnull
    @Override
    public List<SpendJson> findAllByUsername(@Nonnull String username) {
        return spendClient.findAllByUsername(username);
    }

    @Nonnull
    @Override
    public List<SpendJson> findAll() {
        throw new UnsupportedOperationException("Find all spends not supported for API client.");
    }

    @Override
    public SpendJson update(@Nonnull SpendJson spendJson) {
        return spendClient.update(spendJson);
    }

    @Override
    public void remove(@Nonnull SpendJson spendJson) {
        spendClient.remove(spendJson.getUsername(), List.of(spendJson.getId()));
    }

    @Nonnull
    @Override
    public CategoryJson createCategory(@Nonnull CategoryJson categoryJson) {
        return categoryClient.create(categoryJson);
    }

    @Nonnull
    @Override
    public Optional<CategoryJson> findCategoryById(@Nonnull UUID id) {
        try {
            return categoryClient.findById(id);
        } catch (AssertionError ex) {
            return Optional.empty();
        }
    }

    @Nonnull
    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndName(@Nonnull String username, @Nonnull String name) {
        return categoryClient.findByUsernameAndName(username, name);
    }

    @Nonnull
    @Override
    public List<CategoryJson> findAllCategoriesByUsername(@Nonnull String username) {
        return categoryClient.findAllByUsername(username, false);
    }

    @Nonnull
    @Override
    public List<CategoryJson> findAllCategories() {
        throw new UnsupportedOperationException("Find all categories not supported for API client");
    }

    @Override
    public CategoryJson updateCategory(@Nonnull CategoryJson categoryJson) {
        return categoryClient.update(categoryJson);
    }

    @Override
    public void removeCategory(@Nonnull CategoryJson categoryJson) {
        throw new UnsupportedOperationException("Remove category not supported for API client");
    }
}
