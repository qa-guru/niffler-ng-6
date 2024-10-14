package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

@Slf4j
public class UserdataDbClient {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final UserMapper userMapper = new UserMapper();

    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return transaction(connection -> {
                        return 
                        userMapper.toDto(
                                new UserdataUserDaoJdbc(connection).create(
                                        userMapper.toEntity(userModel)));
                },
                USERDATA_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return transaction(connection -> {
                        return 
                        new UserdataUserDaoJdbc(connection)
                                .findById(id)
                                .map(userMapper::toDto);
                },
                USERDATA_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return transaction(connection -> {
                        return 
                        new UserdataUserDaoJdbc(connection)
                                .findByUsername(username)
                                .map(userMapper::toDto);
                },
                USERDATA_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void delete(UUID id) {
        log.info("Remove user by id = [{}]", id);
        transaction(connection -> {
                    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc(connection);
                    userdataUserDao.findById(id)
                            .ifPresent(userdataUserDao::delete);
                    return null;
                },
                USERDATA_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

}