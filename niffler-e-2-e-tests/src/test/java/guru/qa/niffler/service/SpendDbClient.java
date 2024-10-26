package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.imp.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.imp.SpendDaoJdbc;
import guru.qa.niffler.data.entity.category.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;
import static guru.qa.niffler.enums.TransactionLevelEnum.TRANSACTION_SERIALIZABLE;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  public SpendJson createSpend(SpendJson spend) {
    return xaTransaction(TRANSACTION_SERIALIZABLE, new XaFunction<>(
            connection -> {
              final SpendEntity spendEntity = SpendEntity.fromJson(spend);
              if (spendEntity.getCategory().getId() == null) {
                spendEntity.setCategory(new CategoryDaoJdbc(connection).create(spendEntity.getCategory()));
              } else {
                spendEntity.setCategory(CategoryEntity.fromJson(spend.category()));
              }
              if (spend.id() == null) {
                return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
              } else {
                return SpendJson.fromEntity(spendEntity);
              }
            }, CFG.spendJDBCUrl()
        )
    );
  }

  public List<SpendJson> findAllByUsername(String username) {
    return transaction(connection -> {
          return new SpendDaoJdbc(connection).findAllByUsername(username).stream()
              .map(SpendJson::fromEntity)
              .collect(Collectors.toList());
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public SpendJson findSpendById(UUID id) {
    return transaction(connection -> {
          return new SpendDaoJdbc(connection).findSpendById(id)
              .map(SpendJson::fromEntity)
              .orElse(null);
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public void deleteSpend(SpendJson spend) {
    transaction(connection -> {
          new SpendDaoJdbc(connection).deleteSpend(SpendEntity.fromJson(spend));
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }
}
