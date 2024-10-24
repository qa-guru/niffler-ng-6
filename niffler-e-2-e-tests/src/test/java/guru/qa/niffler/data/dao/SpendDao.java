package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;

public interface SpendDao {
  SpendEntity create(SpendEntity spend);

  List<SpendEntity> findAll();
}
