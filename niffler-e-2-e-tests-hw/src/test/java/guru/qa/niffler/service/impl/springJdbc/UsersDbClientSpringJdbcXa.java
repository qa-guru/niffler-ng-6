package guru.qa.niffler.service.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UsersDbClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsersDbClientSpringJdbcXa implements UsersDbClient {

    private static final AuthUserMapper authUserMapper = new AuthUserMapper();
    private static final UserMapper userMapper = new UserMapper();
    private static final Config CFG = Config.getInstance();
    private static final String AUTH_JDBC_URL = CFG.authJdbcUrl();
    private static final String USERDATA_JDBC_URL = CFG.authJdbcUrl();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL, USERDATA_JDBC_URL);

    @Override
    public UserModel createUserInAuthAndUserdataDBs(UserModel userModel) {

        log.info("Create new user in niffler-auth and niffler-userdata: {}", userModel);

        return xaTxTemplate.execute(() -> {

            AuthUserEntity authUserEntity = new AuthUserDaoSpringJdbc().create(
                    authUserMapper.toEntity(
                            userMapper.toAuthDto(userModel)));
            new AuthAuthorityDaoSpringJdbc().create(
                    AuthAuthorityEntity.builder()
                            .userId(authUserEntity.getId())
                            .authority(Authority.read)
                            .build(),
                    AuthAuthorityEntity.builder()
                            .userId(authUserEntity.getId())
                            .authority(Authority.write)
                            .build());

            // Create userdata user
            return userMapper.toDto(new UserdataUserDaoSpringJdbc()
                    .create(userMapper.toEntity(userModel)));

        });

    }

    @Override
    public void deleteUserFromAuthAndUserdataDBs(UserModel userModel) {

        log.info("Remove user in niffler-auth and niffler-userdata: {}", userModel);

        xaTxTemplate.execute(() -> {

            var authUserDao = new AuthUserDaoSpringJdbc();
            var authorityDao = new AuthAuthorityDaoSpringJdbc();
            var userdataDao = new UserdataUserDaoSpringJdbc();

            // Find authorities and auth user if auth user exists
            authUserDao
                    .findByUsername(userModel.getUsername())
                    .ifPresent(authUser -> {
                        authorityDao.delete(
                                authorityDao.findByUserId(authUser.getId())
                                        .toArray(AuthAuthorityEntity[]::new)
                        );
                        authUserDao.delete(authUser);
                    });

            // Remove userdata user
            userdataDao.findByUsername(userModel.getUsername())
                    .ifPresent(userdataDao::delete);

            return null;

        });

    }

}
