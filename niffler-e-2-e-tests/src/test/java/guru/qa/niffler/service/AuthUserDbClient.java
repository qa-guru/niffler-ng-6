package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.Authory;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.AuthAuthorityJson;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;



public class AuthUserDbClient {

    private final Config CFG = Config.getInstance();

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public AuthAuthorityJson create(AuthAuthorityJson authAuthority) {
        return  transaction(connection -> {
            AuthAuthorityEntity authAuthorityEntity = AuthAuthorityEntity.fromJson(authAuthority);
            return AuthAuthorityJson.fromEntity(new AuthAuthorityDaoJdbc(connection).create(authAuthorityEntity));
        }, CFG.authJdbcUrl());
    }

    public AuthUserJson create(AuthUserJson authUser) {
        return transaction(connection -> {
            AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUser);
            return AuthUserJson.fromEntity(new AuthUserDaoJdbc(connection).create(authUserEntity));
        }, CFG.authJdbcUrl());
    }

    public UserJson createUser(UserJson userJson) {
        return UserJson.fromEntity(
                new XaFunction<>(
                        connection -> {
                            AuthUserEntity authUserEntity = new AuthUserEntity();
                            authUserEntity.setUsername(userJson.username());
                            authUserEntity.setPassword(pe.encode("12345"));
                            authUserEntity.setAccountNonExpired(true);
                            authUserEntity.setAccountNonLocked(true);
                            authUserEntity.setCredentialsNonExpired(true);
                            new AuthUserDaoJdbc(connection).create(authUserEntity);
                            new AuthAuthorityDaoJdbc(connection).create(
                                    Arrays.stream(Authory.values())
                                            .map(a -> {
                                                AuthAuthorityEntity authAuthorityEntity = new AuthAuthorityEntity();
                                                authAuthorityEntity.setUserId(authUserEntity.getId());
                                                authAuthorityEntity.setAuthority(a);
                                                return authAuthorityEntity;
                                            }).toArray(AuthAuthorityEntity[]::new)
                            );
                            return  null;
                        },
                        CFG.authJdbcUrl()
                ),
                new XaFunction<>(
                        connection -> {
                                UserEntity ue = new UserEntity();
                                ue.setUsername(userJson.username());
                                ue.setCurrency(userJson.currency());
                                ue.setFullname(userJson.fullname());
                                new UserDaoJdbc(connection).createUser(ue);
                                return ue;
                        },
                        CFG.userdataJdbcUrl()
                )
        ,null);
     }

}
