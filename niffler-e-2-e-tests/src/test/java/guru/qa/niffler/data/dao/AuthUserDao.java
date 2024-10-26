package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;

public interface AuthUserDao {
  AuthUserEntity create(AuthUserEntity authUser);
  Optional<AuthUserEntity> findUserByUsername(String username);
  void deleteUserByUsername(String username);
}
