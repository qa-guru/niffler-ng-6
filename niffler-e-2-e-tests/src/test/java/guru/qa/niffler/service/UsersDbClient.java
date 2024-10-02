package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // Spring JDBC DAOs
    private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
    private final UdUserDao udUserDaoSpringJdbc = new UdUserDaoSpringJdbc();

    // JDBC DAOs
    private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
    private final UdUserDao udUserDaoJdbc = new UdUserDaoJdbc();

    TransactionTemplate txTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    // 1. Spring JDBC с транзакциями
    public UserJson createUserWithSpringJdbcTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(null);
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDaoSpringJdbc.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDaoSpringJdbc.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    // 2. Spring JDBC без транзакций
    public UserJson createUserWithoutSpringJdbcTransaction(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDaoSpringJdbc.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDaoSpringJdbc.create(UserEntity.fromJson(user)),
                null
        );
    }

    // 3. JDBC с транзакциями
    public UserJson createUserWithJdbcTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(null);
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDaoJdbc.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDaoJdbc.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    // 4. JDBC без транзакций
    public UserJson createUserWithoutJdbcTransaction(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDaoJdbc.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDaoJdbc.create(UserEntity.fromJson(user)),
                null
        );
    }
}