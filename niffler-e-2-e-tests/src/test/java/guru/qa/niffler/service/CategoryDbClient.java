package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;


public class CategoryDbClient {
    private final Config CFG = Config.getInstance();

    public CategoryEntity createCategory(CategoryEntity category) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).createCategory(category);
                },
                CFG.spendJdbcUrl()
        );

    }

    public void deleteCategory(CategoryEntity category) {
        transaction(connection -> {
                    new CategoryDaoJdbc(connection).deleteCategory(category);
                },
                CFG.spendJdbcUrl());

    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryById(id);
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<CategoryEntity> findAllByUsername(String username) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl()
        );
    }
}
