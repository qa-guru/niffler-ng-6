package guru.qa.niffler.service.db.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.db.UserdataDbClient;
import lombok.extern.slf4j.Slf4j;

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
    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return xaTxTemplate.execute(() ->
                userMapper.toDto(
                        userRepository.create(userMapper.toEntity(userModel))));
    }

    @Override
    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return xaTxTemplate.execute(() ->
                userRepository
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return xaTxTemplate.execute(() ->
                userRepository
                        .findByUsername(username)
                        .map(userMapper::toDto)
        );
    }

    @Override
    public List<UserModel> findAll() {
        log.info("Get all users");
        return xaTxTemplate.execute(() ->
                userRepository
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList()
        );
    }

    @Override
    public void sendInvitation(UserModel requester, UserModel addressee, FriendshipStatus status) {
        log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
        xaTxTemplate.execute(() -> {

                    userRepository.sendInvitation(
                            userMapper.toEntity(requester),
                            userMapper.toEntity(addressee),
                            status);
                    return null;
                }
        );
    }

    @Override
    public void addFriend(UserModel requester, UserModel addressee) {
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
    public void remove(UserModel user) {
        log.info("Remove user by id: {}", user);
        xaTxTemplate.execute(() -> {
                    userRepository.remove(userMapper.toEntity(user));
                    return null;
                }
        );
    }

}
