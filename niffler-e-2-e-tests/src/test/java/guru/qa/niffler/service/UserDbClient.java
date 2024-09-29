package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserDbClient {
    private final Config CFG = Config.getInstance();


    public UserEntity createUser(UserEntity user) {
        return transaction(connection -> {
                    return new UserDaoJdbc(connection).createUser(user);
                },
                CFG.userDataJdbcUrl()
        );
    }

    public void deleteUser(UserEntity user) {
        transaction(connection -> {
                    new UserDaoJdbc(connection).delete(user);
                },
                CFG.userDataJdbcUrl()
        );
    }

    public Optional<UserEntity> findById(UUID id) {
        return transaction(connection -> {
                    return new UserDaoJdbc(connection).findById(id);
                },
                CFG.userDataJdbcUrl()
        );
    }

    public Optional<UserEntity> findByUsername(String username) {
        return transaction(connection -> {
                    return new UserDaoJdbc(connection).findByUsername(username);
                },
                CFG.userDataJdbcUrl()
        );
    }
}
