package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendClient {
    SpendJson createSpend(SpendJson spend);

    SpendJson updateSpend(SpendJson spend);

    Optional<SpendJson> findSpendById(UUID id);

    List<SpendJson> findSpendByUsernameAndDescription(String username, String description);

    void deleteSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name);

    void deleteCategory(CategoryJson category);
}
