package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
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

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

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
                CFG.spendJdbcUrl()
        );
    }

    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    new SpendDaoJdbc(connection).deleteSpend(spendEntity);
                    return null;
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson spend) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(spend);
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                    return null;
                },
                CFG.spendJdbcUrl()
        );
    }

}
