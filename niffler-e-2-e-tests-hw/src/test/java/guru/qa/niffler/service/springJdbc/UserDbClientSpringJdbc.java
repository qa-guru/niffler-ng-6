package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.dataSource;

@Slf4j
public class UserDbClientSpringJdbc {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private static final AuthUserMapper authUserMapper = new AuthUserMapper();
    private static final UserMapper userMapper = new UserMapper();

    public void createUserInAuthAndUserdataDBs(UserModel userModel) {

        log.info("Create new user in niffler-auth and niffler-userdata: {}", userModel);

        // Create auth user
        var authUserEntity = new AuthUserDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .create(authUserMapper.toEntity(
                        userMapper.toAuthDto(userModel))
                );

        // Create authorities
        new AuthAuthorityDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .create(Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityEntity.builder()
                                        .userId(authUserEntity.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityEntity[]::new));

        // Create userdata user
        new UserdataUserDaoSpringJdbc(dataSource(USERDATA_JDBC_URL))
                .create(userMapper.toEntity(userModel));

    }

    public void deleteUserFromAuthAndUserdataDBs(UserModel userModel) {

        log.info("Remove user in niffler-auth and niffler-userdata: {}", userModel);

        var authUserDao = new AuthUserDaoSpringJdbc(dataSource(AUTH_JDBC_URL));
        var authorityDao = new AuthAuthorityDaoSpringJdbc(dataSource(AUTH_JDBC_URL));
        var userdataDao = new UserdataUserDaoSpringJdbc(dataSource(USERDATA_JDBC_URL));

        authUserDao
                .findByUsername(userModel.getUsername())
                .ifPresent(
                        authUser -> {
                            authorityDao.delete(
                                    authorityDao.findByUserId(authUser.getId())
                                            .toArray(AuthAuthorityEntity[]::new)
                            );
                            authUserDao.delete(authUser);
                        }
                );

        // Remove userdata user
        userdataDao.findByUsername(userModel.getUsername())
                .ifPresent(userdataDao::delete);

    }

}
