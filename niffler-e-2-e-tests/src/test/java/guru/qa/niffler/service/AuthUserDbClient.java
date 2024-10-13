package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepsitoryJdbc;
import guru.qa.niffler.data.repository.impl.UserRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;


public class AuthUserDbClient {

    private final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepsitoryJdbc();
    private final UserDao userDao = new UserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(

                    dataSource(CFG.authJdbcUrl())
            )
    );

    TransactionTemplate ctmTxTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.authJdbcUrl()
    );

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    //JDBC withot transaction
    public UserJson createUserJdbc(UserJson userJson) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(userJson.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        AuthUserEntity createdAuthUser = new AuthUserDaoJdbc().create(authUser);
        AuthAuthorityEntity[] userAuthority = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthAuthorityEntity ae = new AuthAuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthAuthorityEntity[]::new);
        new AuthAuthorityDaoJdbc().create(userAuthority);
        return UserJson.fromEntity(
                new UserDaoJdbc().createUser(
                        UserEntity.fromJson(userJson)
                )
        );
    }


    //JDBC transaction
    public UserJson createUserJdbcTx(UserJson user) {
        jdbcTxTemplate.execute(() -> {
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
                                        ae.setUser(authUser);
                                        ae.setAuthority(a);
                                        return ae;
                                    }
                            ).toArray(AuthAuthorityEntity[]::new));
                    return null;
                }
        );
        UserEntity ue = new UserEntity();
        ue.setUsername(user.username());
        ue.setFullname(user.fullname());
        ue.setCurrency(user.currency());
        new UserDaoJdbc().createUser(ue);
        return UserJson.fromEntity(ue);
    }

    //Spring without transaction
    public UserJson createUserSpring(UserJson userJson) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(userJson.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc().create(authUser);
        AuthAuthorityEntity[] userAuthority = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthAuthorityEntity ae = new AuthAuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthAuthorityEntity[]::new);
        new AuthAuthorityDaoSpringJdbc().create(userAuthority);
        return UserJson.fromEntity(
                userDao.createUser(
                        UserEntity.fromJson(userJson)
                )
        );
    }

    //Spring transaction XA
    public UserJson createUserSpringTxXa(UserJson userJson) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(userJson.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc().create(authUser);
                    AuthAuthorityEntity[] userAuthority = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthAuthorityEntity[]::new);
                    new AuthAuthorityDaoSpringJdbc().create(userAuthority);
                    return UserJson.fromEntity(
                            userDao.createUser(
                                    UserEntity.fromJson(userJson)
                            )
                    );
                }
        );
    }

    public UserJson createUserJdbcCtmTx(UserJson user) {
        return ctmTxTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    new AuthUserDaoSpringJdbc().create(authUser);
                    new AuthAuthorityDaoSpringJdbc().create(Arrays.stream(Authority.values())
                            .map(a -> {
                                        AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                        ae.setUser(authUser);
                                        ae.setAuthority(a);
                                        return ae;
                                    }
                            ).toArray(AuthAuthorityEntity[]::new));
                    UserEntity ue = new UserEntity();
                    ue.setUsername(user.username());
                    ue.setFullname(user.fullname());
                    ue.setCurrency(user.currency());
                    userDao.createUser(ue);
                    return UserJson.fromEntity(ue);
                }
        );
    }

    //Spring transaction
    public UserJson createUserJdbcSpringTx(UserJson user) {
        txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    new AuthUserDaoSpringJdbc().create(authUser);
                    new AuthAuthorityDaoSpringJdbc().create(Arrays.stream(Authority.values())
                            .map(a -> {
                                        AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                        ae.setUser(authUser);
                                        ae.setAuthority(a);
                                        return ae;
                                    }
                            ).toArray(AuthAuthorityEntity[]::new));
                    return null;
                }
        );
        UserEntity ue = new UserEntity();
        ue.setUsername(user.username());
        ue.setFullname(user.fullname());
        ue.setCurrency(user.currency());
        userDao.createUser(ue);
        return UserJson.fromEntity(ue);
    }


    //Jdbc transaction XA from repository
    public UserJson createUserRepository(UserJson userJson) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(userJson.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    authUser.setAuthorities(
                            Arrays.stream(Authority.values()).map(
                                    e -> {
                                        AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                        ae.setUser(authUser);
                                        ae.setAuthority(e);
                                        return ae;
                                    }
                            ).toList()
                    );
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userDao.createUser(
                                    UserEntity.fromJson(userJson)
                            )
                    );
                }
        );
    }

    public void createUsersFriendShipJdbc(UserJson userJson1, UserJson userJson2, FriendshipStatus value) {
         xaTxTemplate.execute(() -> {
            List<UserJson> userList = new ArrayList<>();
            userList.add(userJson1);
            userList.add(userJson2);
            for (UserJson userJson : userList) {
                AuthUserEntity authUser = new AuthUserEntity();
                authUser.setUsername(userJson.username());
                authUser.setPassword(pe.encode("12345"));
                authUser.setEnabled(true);
                authUser.setAccountNonExpired(true);
                authUser.setAccountNonLocked(true);
                authUser.setCredentialsNonExpired(true);
                authUser.setAuthorities(
                        Arrays.stream(Authority.values()).map(
                                e -> {
                                    AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                    ae.setUser(authUser);
                                    ae.setAuthority(e);
                                    return ae;
                                }
                        ).toList()
                );
                authUserRepository.create(authUser);
            }
            if (value.equals(FriendshipStatus.PENDING)) {
                new UserRepositoryJdbc().createUsersFriendshipPending(UserEntity.fromJson(userJson1), UserEntity.fromJson(userJson2));
            } else {
                new UserRepositoryJdbc().createUsersFriendshipAccepted(UserEntity.fromJson(userJson1), UserEntity.fromJson(userJson2));
            }
             return null;
         });
    }

}