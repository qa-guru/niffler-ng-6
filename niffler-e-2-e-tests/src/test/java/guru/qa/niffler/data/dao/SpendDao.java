package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendEntity create(SpendEntity spend);
    Optional<List<SpendEntity>> findAll();
    Optional<SpendEntity> findSpendById(UUID id);
    Optional<List<SpendEntity>> findAllByUsername(String username);
    void deleteSpend(SpendEntity spend);
}
