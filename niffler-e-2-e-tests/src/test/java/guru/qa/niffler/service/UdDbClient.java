package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.imp.UdUserDaoJdbc;
import guru.qa.niffler.data.dao.imp.spring.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.imp.spring.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.imp.spring.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.enums.AuthorityEnum;
import guru.qa.niffler.enums.TransactionLevelEnum;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class UdDbClient {
  private static final Config CFG = Config.getInstance();

  public UserJson createUserSpringJDBC(UserJson user) {
    AuthUserEntity aue = new AuthUserEntity();
    aue.setUsername(user.username());
    aue.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("12345"));
    aue.setEnabled(true);
    aue.setAccountNonExpired(true);
    aue.setAccountNonLocked(true);
    aue.setCredentialsNonExpired(true);

    AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJDBCUrl())).create(aue);

    AuthAuthorityEntity[] userAuthorities = Arrays.stream(AuthorityEnum.values()).map(
        e -> {
          AuthAuthorityEntity ae = new AuthAuthorityEntity();
          ae.setUserId(createdAuthUser.getId());
          ae.setAuthority(e);
          return ae;
        }
    ).toArray(AuthAuthorityEntity[]::new);

    new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJDBCUrl())).create(userAuthorities);

    return UserJson.fromEntity(new UdUserDaoSpringJdbc(dataSource(CFG.userdataJDBCUrl())).create(
        UdUserEntity.fromJson(user)
    ));
  }

  public UserJson createUserdata(UserJson userJson) {
    return transaction(connection -> {
          return UserJson.fromEntity(new UdUserDaoJdbc(connection).create(UdUserEntity.fromJson(userJson)));
        }, CFG.userdataJDBCUrl(), TransactionLevelEnum.TRANSACTION_SERIALIZABLE
    );
  }

  public Optional<UserJson> findById(UUID id) {
    return Optional.ofNullable(transaction(connection -> {
          return UserJson.fromEntity(new UdUserDaoJdbc(connection).findById(id).get());
        }, CFG.userdataJDBCUrl(), TransactionLevelEnum.TRANSACTION_SERIALIZABLE
    ));
  }

  public List<UserJson> findAll() {
    return new UdUserDaoSpringJdbc(dataSource(CFG.userdataJDBCUrl())).findAll().stream()
        .map(UserJson::fromEntity)
        .collect(Collectors.toList());
  }
}
