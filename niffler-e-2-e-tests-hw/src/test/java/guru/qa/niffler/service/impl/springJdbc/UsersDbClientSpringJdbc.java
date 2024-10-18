package guru.qa.niffler.service.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UsersDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public class UsersDbClientSpringJdbc implements UsersDbClient {

    private static final AuthUserMapper authUserMapper = new AuthUserMapper();
    private static final UserMapper userMapper = new UserMapper();
    private static final Config CFG = Config.getInstance();

    private final TransactionTemplate txTemplateAuth = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())));

    private final TransactionTemplate txTemplateUserdata = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(CFG.userdataJdbcUrl())));

    @Override
    public UserModel createUserInAuthAndUserdataDBs(UserModel userModel) {

        log.info("Create new user in niffler-auth and niffler-userdata: {}", userModel);

        txTemplateAuth.execute(status -> {

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
            return null;
        });

        // add user in userdata
        return txTemplateUserdata.execute(status ->
                userMapper.toDto(new UserdataUserDaoJdbc()
                        .create(userMapper.toEntity(userModel))));

    }


    @Override
    public void deleteUserFromAuthAndUserdataDBs(UserModel userModel) {

        log.info("Remove user in niffler-auth and niffler-userdata: {}", userModel);

        // Find authorities and auth user if auth user exists
        txTemplateAuth.execute(status -> {
            var authUserDao = new AuthUserDaoSpringJdbc();
            var authorityDao = new AuthAuthorityDaoSpringJdbc();
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
            return null;
        });

        // Remove userdata user
        txTemplateUserdata.execute(status -> {
            var userdataDao = new UserdataUserDaoSpringJdbc();
            userdataDao
                    .findByUsername(userModel.getUsername())
                    .ifPresent(userdataDao::delete);
            return null;
        });

    }

}
