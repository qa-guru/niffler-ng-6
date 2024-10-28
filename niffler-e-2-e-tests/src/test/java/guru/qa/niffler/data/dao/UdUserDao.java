package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UdUserDao {
  UserEntity create(UserEntity user);

  Optional<UserEntity> findUserdataById(UUID id);

  List<UserEntity> findAllByUsername(String username);

  void deleteUserdata(UserEntity user);
}
