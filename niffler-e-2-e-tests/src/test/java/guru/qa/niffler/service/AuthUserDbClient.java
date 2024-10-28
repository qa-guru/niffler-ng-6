package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.imp.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.imp.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.imp.spring.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.enums.AuthorityEnum;
import guru.qa.niffler.model.AuthUserJson;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;
import static guru.qa.niffler.enums.TransactionLevelEnum.TRANSACTION_SERIALIZABLE;

public class AuthUserDbClient {

  private static final Config CFG = Config.getInstance();

  public AuthUserJson createAuthUser(AuthUserJson authUserJson) {
    return xaTransaction(TRANSACTION_SERIALIZABLE, new XaFunction<>(
            connection -> {
              AuthUserEntity createdAuthUser = new AuthUserDaoJdbc(connection).create(AuthUserEntity.fromJson(authUserJson));
              new AuthAuthorityDaoJdbc(connection).create(
                  Arrays.stream(AuthorityEnum.values())
                      .map(authorityEnum -> {
                        AuthAuthorityEntity ae = new AuthAuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(authorityEnum);
                        return ae;
                      })
                      .toArray(AuthAuthorityEntity[]::new)
              );
              return AuthUserJson.fromEntity(createdAuthUser);
            }, CFG.authJDBCUrl()
        )
    );
  }

  public Optional<AuthUserJson> findById(UUID id) {
    return Optional.ofNullable(transaction(connection -> {
          return new AuthUserDaoJdbc(connection).findById(id)
              .map(AuthUserJson::fromEntity)
              .get();
        }, CFG.authJDBCUrl(), TRANSACTION_SERIALIZABLE
    ));
  }

  public List<AuthUserJson> findAll() {
    return new AuthUserDaoSpringJdbc(dataSource(CFG.authJDBCUrl())).findAll().stream()
        .map(AuthUserJson::fromEntity)
        .collect(Collectors.toList());
  }
}
