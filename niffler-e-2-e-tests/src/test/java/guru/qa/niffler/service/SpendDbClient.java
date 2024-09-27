package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findById(id);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    new SpendDaoJdbc(connection).delete(spendEntity);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                            .create(CategoryEntity.fromJson(category));
                    return CategoryJson.fromEntity(categoryEntity);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findById(id);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findByUsernameAndCategoryName(username, categoryName);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).delete(categoryEntity);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }
}