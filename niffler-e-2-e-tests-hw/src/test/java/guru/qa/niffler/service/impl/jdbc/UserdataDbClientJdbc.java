package guru.qa.niffler.service.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.mapper.AuthAuthorityMapper;
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
public class UserdataDbClientJdbc implements UserdataDbClient {

    private static final UserMapper userMapper = new UserMapper();
    private static final AuthUserMapper authUserMapper = new AuthUserMapper();
    private static final AuthAuthorityMapper authorityMapper = new AuthAuthorityMapper();
    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(USERDATA_JDBC_URL);
    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL, USERDATA_JDBC_URL);
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();


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

    public void sendInvitation(UserModel requester, UserModel addressee, FriendshipStatus status) {
        jdbcTxTemplate.execute(() -> {
                    new UserdataUserDaoJdbc()
                            .sendInvitation(userMapper.toEntity(requester), userMapper.toEntity(addressee), status);
                    return null;
                }
        );
    }

    @Override
    public void getIncomeInvitationFromNewUsers(UserModel requester, int count) {

        if (count > 0) {
            UserEntity requesterEntity = userdataUserDao.findById(
                    requester.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
                            userdataUserDao.sendInvitation(
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
            UserEntity requesterEntity = userdataUserDao.findById(
                    requester.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
                            userdataUserDao.sendInvitation(
                                    addressee,
                                    requesterEntity,
                                    FriendshipStatus.PENDING);
                            return null;
                        }
                );
            }
        }
    }

    public void addFriend(UserModel requester, UserModel addressee) {
        log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
        jdbcTxTemplate.execute(() -> {
                    new UserdataUserDaoJdbc()
                            .addFriend(userMapper.toEntity(requester), userMapper.toEntity(addressee));
                    return null;
                }
        );
    }

    @Override
    public void addNewFriends(UserModel requester, int count) {
        if (count > 0) {
            UserEntity requesterEntity = userdataUserDao.findById(
                    requester.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
                            userdataUserDao.addFriend(
                                    requesterEntity,
                                    addressee);
                            return null;
                        }
                );
            }
        }
    }

    public void remove(UserModel user) {
        log.info("Remove user by id: {}", user);
        jdbcTxTemplate.execute(() -> {
                    new UserdataUserDaoJdbc()
                            .remove(userMapper.toEntity(user));
                    return null;
                }
        );
    }

    private UserEntity createRandomUserIn2Dbs() {

        var generatedUser = UserUtils.generateUser();
        log.info("Creating new user with authorities in niffler-auth and niffler-userdata by DTO: {}", generatedUser);

        var authUser = authUserDao.create(authUserMapper.toEntity(userMapper.toAuthDto(generatedUser)));
        authAuthorityDao.create(
                AuthAuthorityEntity.builder().authority(Authority.read).user(authUser).build(),
                AuthAuthorityEntity.builder().authority(Authority.write).user(authUser).build()
        );

        return userdataUserDao.create(userMapper.toEntity(generatedUser));

    }

}
