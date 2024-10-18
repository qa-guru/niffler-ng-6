package guru.qa.niffler.service.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserdataDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserdataDbClientSpringJdbc implements UserdataDbClient {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(USERDATA_JDBC_URL)));

    private final UserMapper userMapper = new UserMapper();

    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return txTemplate.execute(status ->
                userMapper.toDto(
                        new UserdataUserDaoSpringJdbc()
                                .create(userMapper.toEntity(userModel))));
    }

    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return txTemplate.execute(status ->
                new UserdataUserDaoSpringJdbc()
                        .findById(id)
                        .map(userMapper::toDto));
    }

    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return txTemplate.execute(status ->
                new UserdataUserDaoSpringJdbc()
                        .findByUsername(username)
                        .map(userMapper::toDto));
    }

    public List<UserModel> findAll() {
        log.info("Get all users");
        return txTemplate.execute(status ->
                new UserdataUserDaoSpringJdbc()
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList());
    }

    public void delete(UserModel userModel) {
        log.info("Remove user: {}", userModel);
        txTemplate.execute(status -> {
            new UserdataUserDaoSpringJdbc()
                    .delete(userMapper.toEntity(userModel));
            return null;
        });
    }

}
