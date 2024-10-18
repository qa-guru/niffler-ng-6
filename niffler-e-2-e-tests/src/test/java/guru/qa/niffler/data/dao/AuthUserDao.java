package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {

  AuthUserEntity create(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

}
