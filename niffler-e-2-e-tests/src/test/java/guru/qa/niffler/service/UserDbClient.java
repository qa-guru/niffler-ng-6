package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserDbClient {
    UserDao userDao = new UserDaoJdbc();

    public UserEntity createUser(UserEntity user) {
        return userDao.createUser(user);
    }

    public void deleteUser(UserEntity user) {
        userDao.delete(user);
    }

    public Optional<UserEntity> findById(UUID id) {
        return userDao.findById(id);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
