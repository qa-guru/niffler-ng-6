package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.imp.SpendDaoJdbc;
import guru.qa.niffler.data.entity.category.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDbClient categoryDbClient = new CategoryDbClient();

  public SpendJson createSpend(SpendJson spend) {
    final SpendEntity spendEntity = SpendEntity.fromJson(spend);
    spendEntity.setCategory(CategoryEntity.fromJson(categoryDbClient.createCategory(spend.category())));
    if (spend.id() == null) {
      return SpendJson.fromEntity(spendDao.create(spendEntity));
    } else {
      return SpendJson.fromEntity(spendEntity);
    }
  }

  public List<SpendJson> findAllByUsername(String username) {
    return spendDao.findAllByUsername(username).stream()
        .map(SpendJson::fromEntity)
        .collect(Collectors.toList());
  }

  public SpendJson findSpendById(UUID id) {
    return spendDao.findSpendById(id)
        .map(SpendJson::fromEntity)
        .orElse(null);
  }

  public void deleteSpend(SpendJson spend) {
    spendDao.deleteSpend(SpendEntity.fromJson(spend));
  }
}
