package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.transaction;

public class UserdataDbClient {

private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson user) {
        return transaction(
                connection -> {
                    UserEntity userEntity = UserEntity.fromJson(user);
                    return UserJson.fromEntity(
                            new UserdataUserDaoJdbc(connection).createUser(userEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<UserJson> findUserByUsername(String username) {
        return transaction(
                connection -> {
                    return new UserdataUserDaoJdbc(connection).findByUsername(username).map(UserJson::fromEntity);
                },
                CFG.spendJdbcUrl()
        );
    }


}
