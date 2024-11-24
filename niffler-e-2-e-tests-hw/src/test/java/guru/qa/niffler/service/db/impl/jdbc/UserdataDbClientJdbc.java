package guru.qa.niffler.service.db.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.jdbc.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.db.UserdataDbClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@ParametersAreNonnullByDefault
public class UserdataDbClientJdbc implements UserdataDbClient {

    private static final UserMapper userMapper = new UserMapper();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(USERDATA_JDBC_URL);
    private final UserdataUserRepository userdataRepository = new UserdataUserRepositoryJdbc();

    @Override
    public @Nonnull UserJson create(UserJson userJson) {
        log.info("Creating new user by DTO: {}", userJson);
        return jdbcTxTemplate.execute(() ->
                userMapper.toDto(
                        userdataRepository.create(
                                userMapper.toEntity(userJson)))
        );
    }

    @Override
    public @Nonnull Optional<UserJson> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                userdataRepository
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public @Nonnull Optional<UserJson> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                userdataRepository
                        .findByUsername(username)
                        .map(userMapper::toDto)
        );
    }

    @Override
    public @Nonnull List<UserJson> findAll() {
        log.info("Get all users");
        return jdbcTxTemplate.execute(() ->
                userdataRepository
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList()
        );
    }

    @Override
    public List<UserJson> getIncomeInvitations(String username) {
        log.info("Get user [{}] income invitations", username);
        return jdbcTxTemplate.execute(() ->
                userdataRepository.getIncomeInvitations(userdataRepository
                                .findByUsername(username)
                                .orElseThrow(() ->
                                        new UserNotFoundException("User with username = [" + username + "] not found")))
                        .stream()
                        .map(userMapper::toDto)
                        .toList());
    }

    @Override
    public List<UserJson> getOutcomeInvitations(String username) {
        log.info("Get user [{}] outcome invitations", username);
        return jdbcTxTemplate.execute(() ->
                userdataRepository.getOutcomeInvitations(userdataRepository
                                .findByUsername(username)
                                .orElseThrow(() ->
                                        new UserNotFoundException("User with username = [" + username + "] not found")))
                        .stream()
                        .map(userMapper::toDto)
                        .toList());
    }

    @Override
    public List<UserJson> getFriends(String username) {
        log.info("Get user [{}] friends", username);
        return jdbcTxTemplate.execute(() ->
                userdataRepository.getFriends(userdataRepository
                                .findByUsername(username)
                                .orElseThrow(() ->
                                        new UserNotFoundException("User with username = [" + username + "] not found")))
                        .stream()
                        .map(userMapper::toDto)
                        .toList());
    }

    @Override
    public void sendInvitation(UserJson requester, UserJson addressee) {
        jdbcTxTemplate.execute(() -> {
            userdataRepository.sendInvitation(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee));
            return null;
        });
    }

    @Override
    public void declineInvitation(UserJson requester, UserJson addressee) {
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
    public void addFriend(UserJson requester, UserJson addressee) {
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
    public void unfriend(UserJson requester, UserJson addressee) {
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
    public void remove(UserJson user) {
        log.info("Remove user by id: {}", user);
        jdbcTxTemplate.execute(() -> {
                    userdataRepository.remove(
                            userMapper.toEntity(user));
                    return null;
                }
        );
    }

    @Override
    public void removeAll() {
        log.info("Remove all users");
        jdbcTxTemplate.execute(() -> {
                    userdataRepository.removeAll();
                    return null;
                }
        );
    }

}
