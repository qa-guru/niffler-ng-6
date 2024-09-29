package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    void create(AuthorityEntity... authority);

    Optional<AuthorityEntity> findAuthorityById(UUID id);

    List<AuthorityEntity> findByUserId(String userId);

    void deleteAuthority(AuthorityEntity authority);
}
