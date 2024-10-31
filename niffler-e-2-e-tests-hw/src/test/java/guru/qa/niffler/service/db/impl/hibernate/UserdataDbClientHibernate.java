package guru.qa.niffler.service.db.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.db.UserdataDbClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class UserdataDbClientHibernate implements UserdataDbClient {

    private static final UserMapper userMapper = new UserMapper();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(USERDATA_JDBC_URL);
    private final UserdataUserRepository userRepository = new UserdataUserRepositoryHibernate();

    @Override
    public UserJson create(@Nonnull UserJson userJson) {
        log.info("Creating new user by DTO: {}", userJson);
        return xaTxTemplate.execute(() ->
                userMapper.toDto(
                        userRepository.create(userMapper.toEntity(userJson))));
    }

    @Override
    public Optional<UserJson> findById(@Nonnull UUID id) {
        log.info("Get user by id = [{}]", id);
        return xaTxTemplate.execute(() ->
                userRepository
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public Optional<UserJson> findByUsername(@Nonnull String username) {
        log.info("Get user by username = [{}]", username);
        return xaTxTemplate.execute(() ->
                userRepository
                        .findByUsername(username)
                        .map(userMapper::toDto)
        );
    }

    @Override
    public List<UserJson> findAll() {
        log.info("Get all users");
        return xaTxTemplate.execute(() ->
                userRepository
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList()
        );
    }

    @Override
    public void sendInvitation(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        log.info("Create invitation from [{}] to [{}] with status PENDING", requester.getUsername(), addressee.getUsername());
        xaTxTemplate.execute(() -> {
                    userRepository.sendInvitation(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void declineInvitation(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        log.info("Remove invitation from [{}] to [{}]", requester.getUsername(), addressee.getUsername());
        xaTxTemplate.execute(() -> {
                    userRepository.removeInvitation(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void addFriend(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        xaTxTemplate.execute(() -> {
                    userRepository.addFriend(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void unfriend(@Nonnull UserJson requester, @Nonnull UserJson addressee) {
        log.info("Unfriend: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        xaTxTemplate.execute(() -> {
                    userRepository.removeFriend(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void remove(@Nonnull UserJson user) {
        log.info("Remove user by id: {}", user);
        xaTxTemplate.execute(() -> {
                    userRepository.remove(userMapper.toEntity(user));
                    return null;
                }
        );
    }

}
