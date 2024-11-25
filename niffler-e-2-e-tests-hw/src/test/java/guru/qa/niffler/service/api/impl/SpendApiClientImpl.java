package guru.qa.niffler.service.api.impl;

import guru.qa.niffler.api.internal.CategoryInternalApiClient;
import guru.qa.niffler.api.internal.SpendApiInternalClient;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.service.api.SpendApiClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ParametersAreNonnullByDefault
public class SpendApiClientImpl implements SpendApiClient {

    private final SpendApiInternalClient spendClient = new SpendApiInternalClient();
    private final CategoryInternalApiClient categoryClient = new CategoryInternalApiClient();

    @Override
    public @Nonnull SpendJson create(SpendJson spendJson) {
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


    @Override
    public @Nonnull Optional<SpendJson> findById(UUID id) {
        try {
            return Optional.of(spendClient.findById(id));
        } catch (AssertionError ex) {
            return Optional.empty();
        }
    }


    @Override
    public @Nonnull Optional<SpendJson> findFirstSpendByUsernameAndDescription(String username, String description) {
        return spendClient.findFirstSpendByUsernameAndDescription(username, description);
    }


    @Override
    public @Nonnull List<SpendJson> findAllByUsernameAndDescription(String username, String description) {
        return spendClient.findAllByUsernameAndDescription(username, description);
    }


    @Override
    public @Nonnull List<SpendJson> findAllByUsername(String username) {
        return spendClient.findAllByUsername(username);
    }


    @Override
    public @Nonnull List<SpendJson> findAll() {
        throw new UnsupportedOperationException("Find all spends not supported for API client.");
    }

    @Override
    public @Nonnull SpendJson update(SpendJson spendJson) {
        return spendClient.update(spendJson);
    }

    @Override
    public void remove(SpendJson spendJson) {
        spendClient.remove(spendJson.getUsername(), List.of(spendJson.getId()));
    }


    @Override
    public @Nonnull CategoryJson createCategory(CategoryJson categoryJson) {
        return categoryClient.create(categoryJson);
    }


    @Override
    public @Nonnull Optional<CategoryJson> findCategoryById(UUID id) {
        throw new UnsupportedOperationException("Find category by id not supported for API client");
    }


    @Override
    public @Nonnull Optional<CategoryJson> findCategoryByUsernameAndName(String username, String name) {
        return categoryClient.findByUsernameAndName(username, name);
    }


    @Override
    public @Nonnull List<CategoryJson> findAllCategoriesByUsername(String username) {
        return categoryClient.findAllByUsername(username, false);
    }


    @Override
    public @Nonnull List<CategoryJson> findAllCategories() {
        throw new UnsupportedOperationException("Find all categories not supported for API client");
    }

    @Override
    public @Nonnull CategoryJson updateCategory(CategoryJson categoryJson) {
        return categoryClient.update(categoryJson);
    }

    @Override
    public void removeCategory(CategoryJson categoryJson) {
        throw new UnsupportedOperationException("Remove category not supported for API client");
    }
}
