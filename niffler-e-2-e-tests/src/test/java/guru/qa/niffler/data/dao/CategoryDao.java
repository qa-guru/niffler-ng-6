package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity category);

    Optional<CategoryEntity> findById(UUID id);

    Optional<CategoryEntity> findByUsernameAndCategoryName(String userName, String categoryName);

    List<CategoryEntity> findAllByUsername(String userName);

    List<CategoryEntity> findAll();

    void delete(CategoryEntity category);
}