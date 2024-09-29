package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryJson createCategory(CategoryEntity category);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    List<CategoryJson> findAllByUsername(String username);

    void deleteCategory(CategoryEntity category);
}
