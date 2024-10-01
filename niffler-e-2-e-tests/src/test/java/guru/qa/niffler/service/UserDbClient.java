package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class UserDbClient {
    private final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    Databases.dataSource(CFG.authJdbcUrl())
            )
    );

    public UserJson createUserSpringJdbc(UserJson user) {
        transactionTemplate.execute(status -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                    .create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }
            ).toArray(AuthorityEntity[]::new);
            return null;
        });

        return UserJson.fromEntity(
                new UdUserDaoSpringJdbc(dataSource(CFG.userDataJdbcUrl()))
                        .create(
                                UserEntity.fromJson(user)
                        )
        );
    }

    public UserJson createUser(UserEntity user) {
        return transaction(connection -> {
                    UserEntity userEntity = new UdUserDaoJdbc(connection).create(user);
                    return UserJson.fromEntity(userEntity);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public void deleteUser(UserEntity user) {
        transaction(connection -> {
                    new UdUserDaoJdbc(connection).delete(user);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public Optional<UserJson> findById(UUID id) {
        return transaction(connection -> {
                    Optional<UserEntity> byId = new UdUserDaoJdbc(connection).findById(id);
                    return byId.map(UserJson::fromEntity);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public Optional<UserJson> findByUsername(String username) {
        return transaction(connection -> {
                    Optional<UserEntity> byUsername = new UdUserDaoJdbc(connection).findByUsername(username);
                    return byUsername.map(UserJson::fromEntity);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }
}
