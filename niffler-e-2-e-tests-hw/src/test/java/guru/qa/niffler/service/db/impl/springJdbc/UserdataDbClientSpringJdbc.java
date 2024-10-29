package guru.qa.niffler.service.db.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.springJdbc.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.db.UserdataDbClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserdataDbClientSpringJdbc implements UserdataDbClient {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private static final UserMapper userMapper = new UserMapper();
    private UserdataUserRepository userdataRepository = new UserdataUserRepositorySpringJdbc();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(USERDATA_JDBC_URL))
    );


    @Override
    public UserJson create(@NonNull UserJson userJson) {
        log.info("Creating new user by DTO: {}", userJson);
        return txTemplate.execute(status ->
                userMapper.toDto(
                        userdataRepository.create(
                                userMapper.toEntity(userJson))));
    }

    @Override
    public Optional<UserJson> findById(@NonNull UUID id) {
        log.info("Get user by id = [{}]", id);
        return txTemplate.execute(status ->
                userdataRepository
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public Optional<UserJson> findByUsername(@NonNull String username) {
        log.info("Get user by username = [{}]", username);
        return txTemplate.execute(status ->
                userdataRepository
                        .findByUsername(username)
                        .map(userMapper::toDto));
    }

    @Override
    public List<UserJson> findAll() {
        log.info("Get all users");
        return txTemplate.execute(status ->
                userdataRepository
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList());
    }

    @Override
    public void sendInvitation(@NonNull UserJson requester, @NonNull UserJson addressee) {
        log.info("Send invitation from = [{}] to = [{}]", requester.getUsername());
        txTemplate.execute(status -> {
            userdataRepository.sendInvitation(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee));
            return null;
        });
    }

    @Override
    public void declineInvitation(@NonNull UserJson requester, @NonNull UserJson addressee) {
        log.info("Remove invitation from [{}] to [{}]", requester.getUsername(), addressee.getUsername());
        txTemplate.execute(status -> {
            userdataRepository.removeInvitation(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee));
            return null;
        });
    }

    @Override
    public void addFriend(@NonNull UserJson requester, @NonNull UserJson addressee) {
        log.info("Make friends [{}] and [{}]", requester.getUsername(), addressee.getUsername());
        txTemplate.execute(status -> {
            userdataRepository.addFriend(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee));
            return null;
        });
    }

    @Override
    public void unfriend(@NonNull UserJson requester, @NonNull UserJson addressee) {
        log.info("Unfriend: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        txTemplate.execute(status -> {
            userdataRepository.removeFriend(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee));
            return null;
        });
    }

    @Override
    public void remove(@NonNull UserJson userJson) {
        log.info("Remove user: {}", userJson);
        txTemplate.execute(status -> {
            new UserdataUserDaoSpringJdbc()
                    .remove(userMapper.toEntity(userJson));
            return null;
        });
    }

}
