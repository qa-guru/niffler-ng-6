package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaConsumer;
import guru.qa.niffler.data.dao.imp.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.AuthUserJson;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;
import static guru.qa.niffler.enums.TransactionLevelEnum.TRANSACTION_SERIALIZABLE;

public class AuthUserDbClient {

  private static final Config CFG = Config.getInstance();

  public AuthUserJson createAuthUser(AuthUserJson authUserJson) {
    return transaction(connection -> {
          return AuthUserJson.fromEntity(new AuthUserDaoJdbc(connection).create(AuthUserEntity.fromJson(authUserJson)));
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public AuthUserJson findAuthUserById(String username) {
    return transaction(connection -> {
          return new AuthUserDaoJdbc(connection).findUserByUsername(username)
              .map(AuthUserJson::fromEntity)
              .orElse(null);
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public void deleteAuthUser(AuthUserJson authUserJson) {
    xaTransaction(TRANSACTION_SERIALIZABLE, new XaConsumer(connection -> {
      new AuthAuthorityDbClient().deleteAuthoritiesByUserId(authUserJson.id());
      new AuthUserDaoJdbc(connection).deleteUserByUsername(authUserJson.username());
    }, CFG.spendJDBCUrl()));
  }
}
