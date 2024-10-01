package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;


public class CategoryDbClient {
    private final Config CFG = Config.getInstance();

    public CategoryJson createCategory(CategoryEntity category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).createCategory(category);
                    return CategoryJson.fromEntity(categoryEntity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public void deleteCategory(CategoryEntity category) {
        transaction(connection -> {
                    new CategoryDaoJdbc(connection).deleteCategory(category);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    Optional<CategoryEntity> categoryEntity =
                            new CategoryDaoJdbc(connection)
                                    .findCategoryByUsernameAndCategoryName(username, categoryName);
                    return categoryEntity.map(CategoryJson::fromEntity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        return transaction(connection -> {
                    Optional<CategoryEntity> categoryById = new CategoryDaoJdbc(connection).findCategoryById(id);

                    return categoryById.map(CategoryJson::fromEntity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public List<CategoryJson> findAllByUsername(String username) {
        return transaction(connection -> {
                    List<CategoryEntity> allByUsername = new CategoryDaoJdbc(connection).findAllByUsername(username);
                    return allByUsername.stream()
                            .map(CategoryJson::fromEntity)
                            .toList();
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }
}
