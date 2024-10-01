package guru.qa.niffler.data.dao;

import java.util.UUID;
import guru.qa.niffler.data.entity.auth.UserEntity;

import java.util.Optional;

public interface AuthUserDao {
    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void delete(UUID userId);
}
