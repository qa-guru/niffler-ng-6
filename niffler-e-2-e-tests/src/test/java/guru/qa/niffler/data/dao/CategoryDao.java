package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity category);

    List<CategoryEntity> findAll();

    void deleteCategory(CategoryEntity category);
}