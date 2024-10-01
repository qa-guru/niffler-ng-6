package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import static guru.qa.niffler.data.Databases.transaction;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();


    public SpendJson create(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
                },
                CFG.spendJdbcdUrl(),1);

    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
                },
                CFG.spendJdbcdUrl(),1);
    }

    public CategoryJson updateCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).update(categoryEntity));
                },
                CFG.spendJdbcdUrl(),1);
    }

    public UUID findCategoryByUsernameAndCategoryName(String username, String name) {
        return transaction(connection -> {
                    Optional<CategoryEntity> ce = new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, name);
                    UUID id;
                    if (!ce.isEmpty()) {
                        id = ce.get().getId();
                    } else id = null;
                    return id;
                },
                CFG.spendJdbcdUrl(), 1);
    }


}