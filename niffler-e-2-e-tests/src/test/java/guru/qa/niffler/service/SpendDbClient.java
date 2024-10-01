package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
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
                        var category = new CategoryDaoJdbc(connection)
                                .createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(category);
                    }

                    SpendEntity entity = new SpendDaoJdbc(connection).createSpend(spendEntity);
                    return SpendJson.fromEntity(entity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public void deleteSpend(SpendEntity spend) {
        transaction(connection -> {
                    if (spend.getCategory() != null) {
                        new CategoryDaoJdbc(connection).deleteCategory(spend.getCategory());
                    }
                    new SpendDaoJdbc(connection).deleteSpend(spend);
                }
                , CFG.spendJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        return transaction(connection -> {
                    List<SpendEntity> allByUsername = new SpendDaoJdbc(connection).findAllByUsername(username);
                    return allByUsername.stream()
                            .map(SpendJson::fromEntity)
                            .toList();
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return transaction(connection -> {
                    Optional<SpendEntity> spendById = new SpendDaoJdbc(connection).findSpendById(id);
                    return spendById.map(SpendJson::fromEntity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }
}
