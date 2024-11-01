package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(
                connection -> {
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

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(
                connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(
                            new CategoryDaoJdbc(connection).create(categoryEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson updateCategory(CategoryJson category) {
        return transaction(
                connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(
                            new CategoryDaoJdbc(connection).update(categoryEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<SpendJson> findSpendByUsername(String username) {
        return transaction(
                connection -> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username).stream()
                            .map(SpendJson::fromEntity)
                            .collect(Collectors.toList());
                },
                CFG.spendJdbcUrl()
        );
    }
}
