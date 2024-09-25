package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserDbClient {

    private final UserDao userDao = new UserDaoJdbc();

    public UserEntity createUser(UserEntity user) {
        return userDao.createUser(user);
    }

    public Optional<UserEntity> findUserById(UUID id) {
        return userDao.findById(id);
    }

    public Optional<UserEntity> findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public void deleteUser(UserEntity user) {
        userDao.delete(user);
    }
}
