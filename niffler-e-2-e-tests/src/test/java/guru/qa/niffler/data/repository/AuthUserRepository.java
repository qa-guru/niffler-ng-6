package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

  AuthUserEntity create(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

}
