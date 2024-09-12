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

public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    // Spend
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

    public SpendJson getSpendById(SpendJson spendJson) {
        Optional<SpendEntity> entity = spendDao.findSpendById(spendJson.id());
        return entity.map(SpendJson::fromEntity).orElseThrow();
    }

    public List<SpendJson> getAllSpendsByUsername(String username) {
        List<SpendEntity> spendEntityList = spendDao.findAllByUsername(username);
        return spendEntityList.stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

    public void deleteSpend(SpendJson spend) {
        spendDao.deleteSpend(SpendEntity.fromJson(spend));
    }

    // Category
    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity entity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(categoryDao.create(entity));
    }

    public CategoryJson updateCategory(CategoryJson category) {
        CategoryEntity entity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(categoryDao.updateCategory(entity));
    }

    public CategoryJson getCategoryByUsernameAndCategoryName(String username, String categoryName) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
        return categoryEntity.map(CategoryJson::fromEntity).orElseThrow();
    }

    public List<CategoryJson> getAllCategoriesByUsername(String username) {
        List<CategoryEntity> listCategoryEntity = categoryDao.findAllByUsername(username);
        return listCategoryEntity.stream()
                .map(CategoryJson::fromEntity)
                .toList();
    }

    public void deleteCategory(CategoryJson category) {
        categoryDao.deleteCategory(CategoryEntity.fromJson(category));
    }
}
