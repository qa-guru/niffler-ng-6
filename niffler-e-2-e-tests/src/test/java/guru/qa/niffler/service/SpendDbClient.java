package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {
    private final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity category = new CategoryDaoJdbc(connection)
                                .createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(category);
                    }

                    return SpendJson.fromEntity(new SpendDaoJdbc(connection).createSpend(spendEntity));
                },
                CFG.spendJdbcUrl()
        );

    }

    public void deleteSpend(SpendEntity spend) {
        transaction(connection -> {
                    if (spend.getCategory() != null) {
                        new CategoryDaoJdbc(connection).deleteCategory(spend.getCategory());
                    }
                    new SpendDaoJdbc(connection).deleteSpend(spend);
                }
                , CFG.spendJdbcUrl()
        );

    }

    public List<SpendEntity> findAllByUsername(String username) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl());
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id);
                },
                CFG.spendJdbcUrl());
    }
}
