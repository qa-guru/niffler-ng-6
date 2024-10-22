package guru.qa.niffler.service.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class UserdataDbClientHibernate implements UserdataDbClient {

    private static final UserMapper userMapper = new UserMapper();
    private static final AuthUserMapper authUserMapper = new AuthUserMapper();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL, USERDATA_JDBC_URL);
    private final UserdataUserRepository userRepository = new UserdataUserRepositoryHibernate();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();

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
    public void getIncomeInvitationFromNewUsers(UserModel requester, int count) {

        if (count > 0) {
            UserEntity requesterEntity = userRepository.findById(
                    requester.getId()
            ).orElseThrow(()-> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
                            userRepository.sendInvitation(
                                    requesterEntity,
                                    addressee,
                                    FriendshipStatus.PENDING);
                            return null;
                        }

                );
            }
        }
    }

    @Override
    public void sendOutcomeInvitationToNewUsers(UserModel requester, int count) {

        if (count > 0) {
            UserEntity requesterEntity = userRepository.findById(
                    requester.getId()
            ).orElseThrow(()-> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
                            userRepository.sendInvitation(
                                    addressee,
                                    requesterEntity,
                                    FriendshipStatus.PENDING);
                            return null;
                        }
                );
            }
        }
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
    public void addNewFriends(UserModel requester, int count) {

        if (count > 0) {
            UserEntity requesterEntity = userRepository.findById(
                    requester.getId()
            ).orElseThrow(()-> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
                            userRepository.addFriend(
                                    requesterEntity,
                                    addressee);
                            return null;
                        }
                );
            }
        }
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

    private UserEntity createRandomUserIn2Dbs() {

        var generatedUser = UserUtils.generateUser();
        log.info("Creating new user with authorities in niffler-auth and niffler-userdata by DTO: {}", generatedUser);

        var authUserEntity = authUserMapper.toEntity(userMapper.toAuthDto(generatedUser));
        authUserEntity.setAuthorities(
                List.of(AuthAuthorityEntity.builder().authority(Authority.read).user(authUserEntity).build(),
                        AuthAuthorityEntity.builder().authority(Authority.write).user(authUserEntity).build())
        );

        authUserRepository.create(authUserEntity);
        return userRepository.create(userMapper.toEntity(generatedUser));

    }

}
