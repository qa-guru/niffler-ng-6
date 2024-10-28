package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.imp.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.imp.spring.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.model.AuthAuthorityJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.enums.TransactionLevelEnum.TRANSACTION_SERIALIZABLE;

public class AuthAuthorityDbClient {

  private static final Config CFG = Config.getInstance();

  public void createAuthAuthority(AuthAuthorityJson authAuthorityJson) {
    transaction(connection -> {
          new AuthAuthorityDaoJdbc(connection).create(AuthAuthorityEntity.fromJson(authAuthorityJson));
        }, CFG.authUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public Optional<AuthAuthorityJson> findAuthAuthorityById(UUID userId) {
    return transaction(connection -> {
          return new AuthAuthorityDaoJdbc(connection).findById(userId).stream()
              .map(AuthAuthorityJson::fromEntity)
              .findFirst();
        }, CFG.authJDBCUrl(), TRANSACTION_SERIALIZABLE
    );
  }

  public List<AuthAuthorityJson> findAll() {
    return new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJDBCUrl())).findAll().stream()
        .map(AuthAuthorityJson::fromEntity)
        .collect(Collectors.toList());
  }
}
