package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.implementation.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.model.AuthAuthorityJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();

    // AuthUser


    // Authority
    public AuthAuthorityJson createAuthority(UUID userId, String authority) {
        return Databases.transaction(connection -> {
                    return AuthAuthorityJson.fromEntity(new AuthAuthorityDaoJdbc(connection)
                            .create(userId, authority));
                }, CFG.authJdbcUrl(), Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public List<AuthAuthorityJson> getAuthorityByUserId(UUID uuid) {
        return Databases.transaction(connection -> {
                    Optional<List<AuthAuthorityEntity>> entity = new AuthAuthorityDaoJdbc(connection).findByUserId(uuid);
                    return entity.map(list -> list.stream().map(AuthAuthorityJson::fromEntity)
                            .collect(Collectors.toList())).orElseThrow(() ->
                            new RuntimeException("Authority not found"));
                }, CFG.authJdbcUrl(), Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public void deleteAuthority(AuthAuthorityJson authUser) {
        Databases.transaction(connection -> {
            new AuthAuthorityDaoJdbc(connection).delete(AuthAuthorityEntity.fromJson(authUser));
        }, CFG.authJdbcUrl(), Connection.TRANSACTION_READ_COMMITTED);
    }
}
