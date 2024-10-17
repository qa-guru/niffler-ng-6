package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;

@Slf4j
public class UserdataDbClientSpringJdbc {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private final UserMapper userMapper = new UserMapper();

    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return userMapper.toDto(
                new UserdataUserDaoSpringJdbc(dataSource(USERDATA_JDBC_URL))
                        .create(userMapper.toEntity(userModel)));
    }

    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return new UserdataUserDaoSpringJdbc(dataSource(USERDATA_JDBC_URL))
                .findById(id)
                .map(userMapper::toDto);
    }

    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return new UserdataUserDaoSpringJdbc(dataSource(USERDATA_JDBC_URL))
                .findByUsername(username)
                .map(userMapper::toDto);
    }

    public List<UserModel> findAll() {
        log.info("Get all users");
        return new UserdataUserDaoSpringJdbc(dataSource(USERDATA_JDBC_URL))
                .findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public void delete(UserModel userModel) {
        log.info("Remove user: {}", userModel);
        new UserdataUserDaoSpringJdbc(dataSource(USERDATA_JDBC_URL))
                .delete(userMapper.toEntity(userModel));
    }

}
