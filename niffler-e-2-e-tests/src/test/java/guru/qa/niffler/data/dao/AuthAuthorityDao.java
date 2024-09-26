package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    AuthorityEntity create(AuthorityEntity authority);

    Optional<AuthorityEntity> findById(UUID id);

    List<AuthorityEntity> findAllByUserId(UUID userId);

    void delete(AuthorityEntity authority);
}