package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDbClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public CategoryJson createCategoryJson(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
    }

    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return categoryDao.findAllByUsername(username);
    }

    public void deleteCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }

    public CategoryJson updateCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(categoryDao.updateCategory(categoryEntity));
    }
}
