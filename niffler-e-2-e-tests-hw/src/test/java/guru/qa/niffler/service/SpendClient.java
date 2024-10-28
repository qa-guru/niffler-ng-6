package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendClient {

    SpendJson create(SpendJson spendJson);

    Optional<SpendJson> findById(UUID id);

    Optional<SpendJson> findFirstSpendByUsernameAndDescription(String username, String description);

    List<SpendJson> findAllByUsernameAndDescription(String username, String description);

    List<SpendJson> findAllByUsername(String username);

    List<SpendJson> findAll();

    SpendJson update(SpendJson spendJson);

    void remove(SpendJson spendJson);

    CategoryJson createCategory(CategoryJson spendJson);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndName(String username, String name);

    List<CategoryJson> findAllCategoriesByUsername(String username);

    List<CategoryJson> findAllCategories();

    CategoryJson updateCategory(CategoryJson categoryJson);

    void removeCategory(CategoryJson categoryJson);

}
