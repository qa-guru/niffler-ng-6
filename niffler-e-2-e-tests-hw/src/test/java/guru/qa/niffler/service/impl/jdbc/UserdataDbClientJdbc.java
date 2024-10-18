package guru.qa.niffler.service.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserdataDbClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class UserdataDbClientJdbc implements UserdataDbClient {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(USERDATA_JDBC_URL);
    private final UserMapper userMapper = new UserMapper();

    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return jdbcTxTemplate.execute(() ->
                userMapper.toDto(
                        new UserdataUserDaoJdbc().create(
                                userMapper.toEntity(userModel)))
        );
    }

    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                new UserdataUserDaoJdbc()
                        .findById(id)
                        .map(userMapper::toDto));
    }

    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                new UserdataUserDaoJdbc()
                        .findByUsername(username)
                        .map(userMapper::toDto)
        );
    }

    public List<UserModel> findAll() {
        log.info("Get all users");
        return jdbcTxTemplate.execute(() ->
                new UserdataUserDaoJdbc()
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList()
        );
    }

    public void delete(UserModel user) {
        log.info("Remove user by id: {}", user);
        jdbcTxTemplate.execute(() -> {
                    new UserdataUserDaoJdbc()
                            .delete(userMapper.toEntity(user));
                    return null;
                }
        );
    }

}
