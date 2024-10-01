package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    AuthAuthorityEntity create(UUID userId, String authority);

    Optional<List<AuthAuthorityEntity>> findById(UUID id);

    Optional<List<AuthAuthorityEntity>> findByUserId(UUID userId);

    void update(UUID userId, String authority);

    void delete(AuthAuthorityEntity user);
}
