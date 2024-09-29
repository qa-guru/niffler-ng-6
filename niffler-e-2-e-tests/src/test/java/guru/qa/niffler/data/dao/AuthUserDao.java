package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity user);

    Optional<AuthUserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void deleteUser(UserEntity user);
}
