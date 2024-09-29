package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserDbClient {
    private final Config CFG = Config.getInstance();

    public UserJson createUser(UserEntity user) {
        return transaction(connection -> {
                    UserEntity userEntity = new UserDaoJdbc(connection).createUser(user);
                    return UserJson.fromEntity(userEntity);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public void deleteUser(UserEntity user) {
        transaction(connection -> {
                    new UserDaoJdbc(connection).delete(user);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_REPEATABLE_READ
        );
    }

    public Optional<UserJson> findById(UUID id) {
        return transaction(connection -> {
                    Optional<UserEntity> byId = new UserDaoJdbc(connection).findById(id);
                    return byId.map(UserJson::fromEntity);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public Optional<UserJson> findByUsername(String username) {
        return transaction(connection -> {
                    Optional<UserEntity> byUsername = new UserDaoJdbc(connection).findByUsername(username);
                    return byUsername.map(UserJson::fromEntity);
                },
                CFG.userDataJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }
}
