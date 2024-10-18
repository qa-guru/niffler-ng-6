package guru.qa.niffler.service.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
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
public class UsersDbClientJdbcXa implements UsersDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private final AuthUserMapper authUserMapper = new AuthUserMapper();
    private final UserMapper userMapper = new UserMapper();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL, USERDATA_JDBC_URL);

    @Override
    public UserModel createUserInAuthAndUserdataDBs(UserModel userModel) {

        log.info("Create new user in niffler-auth and niffler-userdata: {}", userModel);

        return xaTxTemplate.execute(() -> {

            AuthUserEntity authUserEntity = new AuthUserDaoJdbc().create(
                    authUserMapper.toEntity(
                            userMapper.toAuthDto(userModel)));
            new AuthAuthorityDaoJdbc().create(
                    AuthAuthorityEntity.builder()
                            .userId(authUserEntity.getId())
                            .authority(Authority.read)
                            .build(),
                    AuthAuthorityEntity.builder()
                            .userId(authUserEntity.getId())
                            .authority(Authority.write)
                            .build());

            // add user in userdata
            return userMapper.toDto(new UserdataUserDaoJdbc()
                    .create(userMapper.toEntity(userModel)));

        });

    }

    @Override
    public void deleteUserFromAuthAndUserdataDBs(UserModel userModel) {

        log.info("Remove user from niffler-auth and niffler-userdata with username = [{}]", userModel.getUsername());

        xaTxTemplate.execute(() -> {
            // remove user, if exists in niffler-auth
            AuthUserDao authUserDao = new AuthUserDaoJdbc();
            AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();

            authUserDao.findByUsername(userModel.getUsername())
                    .ifPresent(authUser -> {
                        authorityDao.delete(
                                authorityDao.findByUserId(authUser.getId())
                                        .toArray(new AuthAuthorityEntity[0])
                        );
                        authUserDao.delete(authUser);
                    });

            // remove user from niffler-userdata, if exists
            UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();
            userdataUserDao.findByUsername(userModel.getUsername())
                    .ifPresent(userdataUserDao::delete);

            return null;
        });


    }

}
