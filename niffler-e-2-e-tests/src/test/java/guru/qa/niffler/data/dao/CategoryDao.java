package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity create);
    CategoryEntity update(CategoryEntity update);
    Optional<CategoryEntity> findCategoryById(UUID id);
    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);
    Optional<List<CategoryEntity>> findAllByUsername(String username);
    void deleteCategory(CategoryEntity category);
}
