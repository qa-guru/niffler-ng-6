package guru.qa.niffler.service.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserdataDbClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class UserdataDbClientHibernate implements UserdataDbClient {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(USERDATA_JDBC_URL);
    private final UserMapper userMapper = new UserMapper();

    @Override
    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return jdbcTxTemplate.execute(() ->
                userMapper.toDto(
                        new UserdataUserRepositoryJdbc().create(
                                userMapper.toEntity(userModel)))
        );
    }

    @Override
    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                new UserdataUserRepositoryJdbc()
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                new UserdataUserRepositoryJdbc()
                        .findByUsername(username)
                        .map(userMapper::toDto)
        );
    }

    @Override
    public List<UserModel> findAll() {
        log.info("Get all users");
        return jdbcTxTemplate.execute(() ->
                new UserdataUserRepositoryJdbc()
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList()
        );
    }

    @Override
    public void delete(UserModel user) {
        log.info("Remove user by id: {}", user);
        jdbcTxTemplate.execute(() -> {
                    new UserdataUserRepositoryJdbc()
                            .delete(userMapper.toEntity(user));
                    return null;
                }
        );
    }

    public void addFriend(UserModel requester, UserModel addressee) {
        log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        jdbcTxTemplate.execute(() -> {
                    new UserdataUserRepositoryJdbc()
                            .addFriend(userMapper.toEntity(requester), userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    public void createInvitation(UserModel requester, UserModel addressee) {
        log.info("Create invitation from [{}] to [{}]", requester.getUsername(), addressee.getUsername());
        jdbcTxTemplate.execute(() -> {
                    new UserdataUserRepositoryJdbc()
                            .createInvitation(userMapper.toEntity(requester), userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

}
