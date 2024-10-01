package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.implementation.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.implementation.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.implementation.UserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;

import java.sql.Connection;
import java.util.Arrays;

public class UserdataDbClient {

    private static final Config CFG = Config.getInstance();

    public UserJson createUser(AuthUserJson authUser) {
        return (UserJson) Databases.xaTransaction(Connection.TRANSACTION_READ_COMMITTED, new Databases.XaFunction<>(
                        connection -> {
                            AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUser);
                            AuthUserJson authUserJson = AuthUserJson.fromEntity(new AuthUserDaoJdbc(connection)
                                    .create(authUserEntity));
                            Arrays.stream(Authority.values()).forEach(authority ->
                                    new AuthAuthorityDaoJdbc(connection)
                                            .create(authUserJson.id(), authority.name()));
                            return authUserJson;
                        }, CFG.authJdbcUrl()),
                new Databases.XaFunction<>(connection -> {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setUsername(authUser.username());
                    userEntity.setCurrency(CurrencyValues.RUB);
                    return UserJson.fromEntity(new UserDaoJdbc(connection).create(userEntity));
                }, CFG.userdataJdbcUrl())
        );
    }
}
