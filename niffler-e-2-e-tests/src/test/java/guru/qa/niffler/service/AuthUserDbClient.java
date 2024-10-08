package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.UserEntity;

import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class AuthUserDbClient {

    private final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UserDao userDao = new UserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    Databases.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUserSpring(UserJson userJson) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(userJson.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    AuthUserEntity createdAuthUser = authUserDao.create(authUser);
                    AuthAuthorityEntity[] userAuthority = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthAuthorityEntity[]::new);
                    authAuthorityDao.create(userAuthority);
                    return UserJson.fromEntity(
                            userDao.createUser(
                                    UserEntity.fromJson(userJson)
                            )
                    );
                }
        );
    }

    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(
                        new XaFunction<>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword(pe.encode("12345"));
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc().create(authUser);
                                    new AuthAuthorityDaoJdbc().create(Arrays.stream(Authority.values())
                                            .map(a -> {
                                                        AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                                        ae.setUserId(authUser.getId());
                                                        ae.setAuthority(a);
                                                        return ae;
                                                    }
                                            ).toArray(AuthAuthorityEntity[]::new));

                                    return null;
                                },
                                CFG.authJdbcUrl(), 1
                        ),
                        new XaFunction<>(
                                con -> {
                                    UserEntity ue = new UserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserDaoJdbc().createUser(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl(), 1
                        )
                )
        );
    }

}
