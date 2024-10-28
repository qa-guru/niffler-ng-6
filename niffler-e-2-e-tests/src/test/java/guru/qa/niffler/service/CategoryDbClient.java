package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.imp.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.category.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.enums.TransactionLevelEnum.TRANSACTION_SERIALIZABLE;

public class CategoryDbClient {

  private static final Config CFG = Config.getInstance();

  public CategoryJson createCategory(CategoryJson category) {
    return transaction(connection -> {
          if (category.id() == null) {
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(category)));
          } else {
            return category;
          }
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public List<CategoryJson> findAllByUsername(String username) {
    return transaction(connection -> {
      return new CategoryDaoJdbc(connection).findAllByUsername(username).stream()
          .map(CategoryJson::fromEntity)
          .collect(Collectors.toList());
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public Optional<CategoryJson> findCategoryById(UUID id) {
    return Optional.ofNullable(transaction(connection -> {
          return new CategoryDaoJdbc(connection).findCategoryById(id)
              .map(CategoryJson::fromEntity)
              .orElse(null);
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    ));
  }

  public void deleteCategory(CategoryJson category) {
    transaction(connection -> {
      new CategoryDaoJdbc(connection).deleteCategory(CategoryEntity.fromJson(category));
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }
}
