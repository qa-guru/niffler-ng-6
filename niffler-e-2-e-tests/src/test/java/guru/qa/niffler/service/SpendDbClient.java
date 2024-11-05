package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.dataSource;
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
                CFG.spendJdbcUrl(),
                2
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
                CFG.spendJdbcUrl(),
                2
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
                CFG.spendJdbcUrl(),
                2
        );
    }

    public Optional<List<SpendJson>> findSpendingByUsername(String username) {
        return transaction(
                connection -> {
                    Optional<List<SpendEntity>> spendEntities = new SpendDaoJdbc(connection)
                            .findAllByUsername(username);
                    return spendEntities.map(spending ->
                            spending.stream()
                                    .map(SpendJson::fromEntity)
                                    .collect(Collectors.toList())
                    );
                },
                CFG.spendJdbcUrl(),
                2
        );
    }

    public Optional<List<SpendJson>> findAllSpending() {

        Optional<List<SpendEntity>> allSpending = new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .findAll();

        return allSpending.map(spending ->
                spending.stream()
                        .map(SpendJson::fromEntity)
                        .toList()
        );
    }
}
