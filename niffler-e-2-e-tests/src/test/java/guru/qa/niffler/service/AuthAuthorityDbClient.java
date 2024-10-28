package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaConsumer;
import guru.qa.niffler.data.dao.imp.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.model.AuthAuthorityJson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;
import static guru.qa.niffler.enums.TransactionLevelEnum.TRANSACTION_SERIALIZABLE;

public class AuthAuthorityDbClient {

  private static final Config CFG = Config.getInstance();

  public AuthAuthorityJson createAuthAuthority(AuthAuthorityJson authAuthorityJson) {
    return transaction(connection -> {
          return AuthAuthorityJson.fromEntity(new AuthAuthorityDaoJdbc(connection).create(AuthAuthorityEntity.fromJson(authAuthorityJson)));
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public List<AuthAuthorityJson> findAuthAuthorityById(UUID userId) {
    return transaction(connection -> {
          return new AuthAuthorityDaoJdbc(connection).findAuthoritiesByUserId(userId).stream()
              .map(AuthAuthorityJson::fromEntity)
              .collect(Collectors.toList());
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public void deleteAuthority(UUID id) {
    transaction(connection -> {
          new AuthAuthorityDaoJdbc(connection).deleteAuthorityById(id);
        }, CFG.spendJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public void deleteAuthoritiesByUserId(UUID userId) {
    xaTransaction(TRANSACTION_SERIALIZABLE, new XaConsumer(connection -> {
          AuthAuthorityDaoJdbc authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc(connection);
          for (AuthAuthorityEntity aa : authAuthorityDaoJdbc.findAuthoritiesByUserId(userId))
            authAuthorityDaoJdbc.deleteAuthorityById(aa.getId());
        }, CFG.spendJDBCUrl())
    );
  }
}
