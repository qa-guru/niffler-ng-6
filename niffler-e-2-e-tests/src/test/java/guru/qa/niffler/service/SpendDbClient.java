package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.imp.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.imp.SpendDaoJdbc;
import guru.qa.niffler.data.dao.imp.spring.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.category.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.dataSource;
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
              return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
            }, CFG.spendJDBCUrl()
        )
    );
  }

  public Optional<SpendJson> findSpendById(UUID id) {
    return Optional.ofNullable(transaction(connection -> {
          return new SpendDaoJdbc(connection).findById(id)
              .map(SpendJson::fromEntity)
              .orElse(null);
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    ));
  }

  public List<SpendJson> findAll() {
    return new SpendDaoSpringJdbc(dataSource(CFG.spendJDBCUrl())).findAll().stream()
        .map(SpendJson::fromEntity)
        .collect(Collectors.toList());
  }

  public void deleteSpend(SpendJson spend) {
    transaction(connection -> {
          new SpendDaoJdbc(connection).deleteById(spend.id());
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }
}
