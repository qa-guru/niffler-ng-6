package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

public interface SpendDao {
  SpendEntity create(SpendEntity spend);
}
