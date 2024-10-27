package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {

    void create(AuthAuthorityEntity... authority);

    Optional<AuthAuthorityEntity> findById(UUID id);

    List<AuthAuthorityEntity> findByUserId(UUID userId);

    List<AuthAuthorityEntity> findAll();

    void update(AuthAuthorityEntity... authority);

    void remove(AuthAuthorityEntity... authority);

}
