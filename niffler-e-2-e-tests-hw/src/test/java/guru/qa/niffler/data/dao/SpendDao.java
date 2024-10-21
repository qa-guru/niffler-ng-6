package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    SpendEntity create(SpendEntity entity);

    Optional<SpendEntity> findById(UUID id);

    List<SpendEntity> findByUsernameAndDescription(String username, String description);

    List<SpendEntity> findAllByUsername(String username);

    List<SpendEntity> findAll();

    void delete(SpendEntity spend);

}
