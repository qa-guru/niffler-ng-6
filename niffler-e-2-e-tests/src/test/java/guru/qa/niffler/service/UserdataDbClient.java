package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.dao.imp.UserdataDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserdataDbClient {
  private final UserdataDao userdataDao = new UserdataDaoJdbc();

  public UserJson createUserdata(UserJson userJson) {
    if (userJson.id() == null) {
      return UserJson.fromEntity(userdataDao.create(UserEntity.fromJson(userJson)));
    } else {
      return userJson;
    }
  }

  public List<UserJson> findAllByUsername(String username) {
    return userdataDao.findAllByUsername(username).stream()
        .map(UserJson::fromEntity)
        .collect(Collectors.toList());
  }

  public UserJson findUserdataById(UUID id) {
    return userdataDao.findUserdataById(id)
        .map(UserJson::fromEntity)
        .orElse(null);
  }

  public void deleteUserdata(UserJson Userdata) {
    userdataDao.deleteUserdata(UserEntity.fromJson(Userdata));
  }
}
