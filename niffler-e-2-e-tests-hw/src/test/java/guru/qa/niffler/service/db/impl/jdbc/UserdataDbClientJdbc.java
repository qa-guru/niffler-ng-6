package guru.qa.niffler.service.db.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.db.UserdataDbClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;


@Slf4j
public class UserdataDbClientJdbc implements UserdataDbClient {

    private static final UserMapper userMapper = new UserMapper();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(USERDATA_JDBC_URL);
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    @Override
    public UserJson create(@NonNull UserJson userJson) {
        log.info("Creating new user by DTO: {}", userJson);
        return jdbcTxTemplate.execute(() ->
                userMapper.toDto(
                        userdataUserDao.create(
                                userMapper.toEntity(userJson)))
        );
    }

    @Override
    public Optional<UserJson> findById(@NonNull UUID id) {
        log.info("Get user by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                userdataUserDao
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public Optional<UserJson> findByUsername(@NonNull String username) {
        log.info("Get user by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                userdataUserDao
                        .findByUsername(username)
                        .map(userMapper::toDto)
        );
    }

    @Override
    public List<UserJson> findAll() {
        log.info("Get all users");
        return jdbcTxTemplate.execute(() ->
                userdataUserDao
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList()
        );
    }

    @Override
    public void sendInvitation(@NonNull UserJson requester, @NonNull UserJson addressee) {
        jdbcTxTemplate.execute(() -> {
            userdataUserDao.sendInvitation(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee),
                    PENDING);
            return null;
        });
    }

    @Override
    public void addFriend(@NonNull UserJson requester, @NonNull UserJson addressee) {
        log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        jdbcTxTemplate.execute(() -> {
                    userdataUserDao.addFriend(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void remove(@NonNull UserJson user) {
        log.info("Remove user by id: {}", user);
        jdbcTxTemplate.execute(() -> {
                    userdataUserDao.remove(
                            userMapper.toEntity(user));
                    return null;
                }
        );
    }

}
