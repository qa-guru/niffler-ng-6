package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.AuthAuthorityJson;
import guru.qa.niffler.model.AuthUserJson;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;



public class AuthUserDbClient {

    private final Config CFG = Config.getInstance();

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

    public AuthUserJson createUser(AuthUserJson authUser, AuthAuthorityJson...authAuthority) {

        return  xaTransaction(ut -> {
            for(int i=0; i< authAuthority.length; i++ )
            {
                return transaction(connection -> {
                AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUser);
                return AuthUserJson.fromEntity(new AuthUserDaoJdbc(connection).create(authUserEntity));
            }, CFG.authJdbcUrl());
            }
       });
     }




}
