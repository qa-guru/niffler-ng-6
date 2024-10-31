package guru.qa.niffler.service.db.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.jdbc.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.db.UserdataDbClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class UserdataDbClientJdbc implements UserdataDbClient {

    private static final UserMapper userMapper = new UserMapper();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(USERDATA_JDBC_URL);
    private final UserdataUserRepository userdataRepository = new UserdataUserRepositoryJdbc();

    @Override
    public UserJson create(@Nonnull UserJson userJson) {
        log.info("Creating new user by DTO: {}", userJson);
        return jdbcTxTemplate.execute(() ->
                userMapper.toDto(
                        userdataRepository.create(
                                userMapper.toEntity(userJson)))
        );
    }

    @Override
    public Optional<UserJson> findById(@Nonnull UUID id) {
        log.info("Get user by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                userdataRepository
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public Optional<UserJson> findByUsername(@Nonnull String username) {
        log.info("Get user by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                userdataRepository
                        .findByUsername(username)
                        .map(userMapper::toDto)
        );
    }

    @Override
    public List<UserJson> findAll() {
        log.info("Get all users");
        return jdbcTxTemplate.execute(() ->
                userdataRepository
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList()
        );
    }

    @Override
    public void sendInvitation(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        jdbcTxTemplate.execute(() -> {
            userdataRepository.sendInvitation(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee));
            return null;
        });
    }

    @Override
    public void declineInvitation(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        log.info("Remove invitation from [{}] to [{}]", requester.getUsername(), addressee.getUsername());
        jdbcTxTemplate.execute(() -> {
                    userdataRepository.removeInvitation(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void addFriend(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        jdbcTxTemplate.execute(() -> {
                    userdataRepository.addFriend(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void unfriend(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        log.info("Unfriend: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        jdbcTxTemplate.execute(() -> {
                    userdataRepository.removeFriend(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }
    
    @Override
    public void remove(@Nonnull UserJson user) {
        log.info("Remove user by id: {}", user);
        jdbcTxTemplate.execute(() -> {
                    userdataRepository.remove(
                            userMapper.toEntity(user));
                    return null;
                }
        );
    }

}
