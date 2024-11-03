package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;

public class UserdataDbClient {

    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    public UserJson createUser(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return UserJson.fromEntity(
                userdataUserDao.createUser(userEntity)
        );
    }

    public Optional<UserJson> findUserByUsername(String username) {
        return userdataUserDao.findByUsername(username).map(UserJson::fromEntity);
    }


}
