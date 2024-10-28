package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.imp.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.enums.TransactionLevelEnum;
import guru.qa.niffler.model.UserJson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.transaction;

public class UdDbClient {
  private static final Config CFG = Config.getInstance();

  public UserJson createUserdata(UserJson userJson) {
    return transaction(connection -> {
          return UserJson.fromEntity(new UdUserDaoJdbc(connection).create(UserEntity.fromJson(userJson)));
        }, CFG.spendJDBCUrl(), TransactionLevelEnum.TRANSACTION_SERIALIZABLE
    );
  }

  public List<UserJson> findAllByUsername(String username) {
    return transaction(connection -> {
          return new UdUserDaoJdbc(connection).findAllByUsername(username).stream()
              .map(UserJson::fromEntity)
              .collect(Collectors.toList());
        }, CFG.spendJDBCUrl(), TransactionLevelEnum.TRANSACTION_SERIALIZABLE
    );
  }

  public UserJson findUserdataById(UUID id) {
    return transaction(connection -> {
          return new UdUserDaoJdbc(connection).findUserdataById(id)
              .map(UserJson::fromEntity)
              .orElse(null);
        }, CFG.spendJDBCUrl(), TransactionLevelEnum.TRANSACTION_SERIALIZABLE
    );
  }

  public void deleteUserdata(UserJson Userdata) {
    transaction(connection -> {
          new UdUserDaoJdbc(connection).deleteUserdata(UserEntity.fromJson(Userdata));
        }, CFG.spendJDBCUrl(), TransactionLevelEnum.TRANSACTION_SERIALIZABLE
    );
  }
}
