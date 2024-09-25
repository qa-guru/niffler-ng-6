package guru.qa.niffler.service;


import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                spendDao.create(spendEntity)
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findById(id);
    }

    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return spendDao.findAllByUsername(username);
    }

    public void deleteSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        spendDao.delete(spendEntity);
    }

    public CategoryJson createCategory(CategoryJson category) {
        return CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(category)));
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findById(id);
    }

    public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findByUsernameAndCategoryName(username, categoryName);
    }

    public void deleteCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryDao.delete(categoryEntity);
    }
}