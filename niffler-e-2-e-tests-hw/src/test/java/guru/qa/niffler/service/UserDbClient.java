package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserDbClient {

    private final UserMapper userMapper = new UserMapper();
    private final UserDao userDao = new UserDaoJdbc();

    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return userMapper.toDto(userDao.create(userMapper.toEntity(userModel)));
    }

    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return userDao.findById(id).map(userMapper::toDto);
    }

    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return userDao.findByUsername(username).map(userMapper::toDto);
    }

    public void delete(UUID id) {
        log.info("Remove user by id = [{}]", id);
        Optional.ofNullable(userDao.findById(id))
                .ifPresent(category -> userDao.delete(category.orElse(null)));
    }

}
