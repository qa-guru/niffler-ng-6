package guru.qa.niffler.service.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.xaTransaction;

@Slf4j
public class UserDbClient {

    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private final AuthUserMapper authUserMapper = new AuthUserMapper();
    private final UserMapper userMapper = new UserMapper();

    public void createUserInAuthAndUserdataDBs(
            UserModel userModel
    ) {

        log.info("Create new user in niffler-auth and niffler-userdata: {}", userModel);

        xaTransaction(

                TRANSACTION_ISOLATION_LEVEL,

                // add user in auth db
                new XaFunction<>(connection -> {
                    AuthUserEntity authUserEntity = new AuthUserDaoJdbc(connection).create(
                            authUserMapper.toEntity(
                                    userMapper.toAuthDto(userModel)));
                    new AuthAuthorityDaoJdbc(connection).create(
                            AuthAuthorityEntity.builder()
                                    .userId(authUserEntity.getId())
                                    .authority(Authority.read)
                                    .build(),
                            AuthAuthorityEntity.builder()
                                    .userId(authUserEntity.getId())
                                    .authority(Authority.write)
                                    .build());
                    return null;
                },
                        AUTH_JDBC_URL),

                // add user in userdata
                new XaFunction<>(connection ->
                        new UserdataUserDaoJdbc(connection).create(
                                userMapper.toEntity(userModel)),
                        USERDATA_JDBC_URL));

    }

    public void deleteUserFromAuthAndUserdataDBs(UserModel userModel) {

        log.info("Remove user from niffler-auth and niffler-userdata with username = [{}]", userModel.getUsername());

//        transaction(connection -> {
//                    new AuthUserDaoJdbc(connection)
//                            .findByUsername(userModel.getUsername())
//                            .ifPresent(user -> {
//                                AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc(connection);
//                                authAuthorityDao.delete(
//                                        authAuthorityDao.findByUserId(user.getId())
//                                                .toArray(new AuthAuthorityEntity[0])
//                                );
//                            });
//                },
//                AUTH_JDBC_URL,
//                TRANSACTION_ISOLATION_LEVEL
//        );

        xaTransaction(

                TRANSACTION_ISOLATION_LEVEL,

                new XaFunction<>(connection -> {
                    new AuthUserDaoJdbc(connection)
                            .findByUsername(userModel.getUsername())
                            .ifPresent(user -> {
                                AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc(connection);
                                authAuthorityDao.delete(
                                        authAuthorityDao.findByUserId(user.getId())
                                                .toArray(new AuthAuthorityEntity[0])
                                );
                            });
                    return null;
                },
                        AUTH_JDBC_URL
                ),

                // remove user, if exists in niffler-auth
                new XaFunction<>(connection -> {
                    AuthUserDao authUserDao = new AuthUserDaoJdbc(connection);
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(connection);
                    authUserDao.findByUsername(userModel.getUsername()).ifPresent(
                            authUser -> {
                                authorityDao.delete(
                                        authorityDao.findByUserId(authUser.getId())
                                                .toArray(new AuthAuthorityEntity[0])
                                );
                                authUserDao.delete(authUser);
                            }
                    );
                    return null;
                },
                        AUTH_JDBC_URL
                ),

                // remove user from niffler-userdata, if exists
                new XaFunction<>(connection -> {
                    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc(connection);
                    userdataUserDao.findByUsername(userModel.getUsername())
                            .ifPresent(userdataUserDao::delete);
                    return null;
                },
                        USERDATA_JDBC_URL
                )
        );

    }

}
