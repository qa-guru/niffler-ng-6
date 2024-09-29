package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendJson createSpend(SpendEntity spend);

    Optional<SpendJson> findSpendById(UUID id);

    List<SpendJson> findAllByUsername(String username);

    void deleteSpend(SpendEntity spend);
}
