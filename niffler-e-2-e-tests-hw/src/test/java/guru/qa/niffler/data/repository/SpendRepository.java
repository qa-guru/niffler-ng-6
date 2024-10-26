package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findById(UUID id);

    Optional<SpendEntity> findFirstSpendByUsernameAndDescription(String username, String description);

    List<SpendEntity> findAllByUsername(String username);

    List<SpendEntity> findByUsernameAndDescription(String username, String description);

    List<SpendEntity> findAll();

    SpendEntity update(SpendEntity spend);

    void remove(SpendEntity spend);

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndName(String username, String name);

    List<CategoryEntity> findAllCategoriesByUsername(String username);

    List<CategoryEntity> findAllCategories();

    CategoryEntity updateCategory(CategoryEntity category);

    void removeCategory(CategoryEntity category);

}
