package guru.qa.niffler.service.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.UserdataDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserdataDbClientSpringJdbc implements UserdataDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private static final UserMapper userMapper = new UserMapper();
    private static final AuthUserMapper authUserMapper = new AuthUserMapper();

    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL, USERDATA_JDBC_URL);
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(USERDATA_JDBC_URL))
    );


    @Override
    public UserModel create(UserModel userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return txTemplate.execute(status ->
                userMapper.toDto(
                        userdataUserDao.create(
                                userMapper.toEntity(userModel))));
    }

    @Override
    public Optional<UserModel> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return txTemplate.execute(status ->
                userdataUserDao
                        .findById(id)
                        .map(userMapper::toDto));
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return txTemplate.execute(status ->
                userdataUserDao
                        .findByUsername(username)
                        .map(userMapper::toDto));
    }

    @Override
    public List<UserModel> findAll() {
        log.info("Get all users");
        return txTemplate.execute(status ->
                userdataUserDao
                        .findAll().stream()
                        .map(userMapper::toDto)
                        .toList());
    }

    @Override
    public void sendInvitation(UserModel requester, UserModel addressee, FriendshipStatus friendshipStatus) {
        log.info("Send invitation from = [{}] to = [{}]", requester.getUsername(), addressee.getUsername());
        txTemplate.execute(status -> {
            userdataUserDao.sendInvitation(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee),
                    friendshipStatus);
            return null;
        });
    }

    @Override
    public void getIncomeInvitationFromNewUsers(UserModel requester, int count) {

        if (count > 0) {

            UserEntity requesterEntity = userdataUserDao.findById(requester.getId())
                    .orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    var addressee = createRandomUserIn2Dbs();
                    log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
                    userdataUserDao.sendInvitation(
                            requesterEntity,
                            addressee,
                            FriendshipStatus.PENDING);
                    return null;
                });
            }

        }

    }

    @Override
    public void sendOutcomeInvitationToNewUsers(UserModel requester, int count) {
        if (count > 0) {

            UserEntity requesterEntity = userdataUserDao.findById(requester.getId())
                    .orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    var addressee = createRandomUserIn2Dbs();
                    userdataUserDao.sendInvitation(
                            addressee,
                            requesterEntity,
                            FriendshipStatus.PENDING);
                    return null;
                });
            }

        }
    }


    @Override
    public void addFriend(UserModel requester, UserModel addressee) {
        log.info("Make friends [{}] and [{}]", requester.getUsername(), addressee.getUsername());
        txTemplate.execute(status -> {
            userdataUserDao.addFriend(
                    userMapper.toEntity(requester),
                    userMapper.toEntity(addressee));
            return null;
        });
    }

    @Override
    public void addNewFriends(UserModel requester, int count) {

        if (count > 0) {

            UserEntity requesterEntity = userdataUserDao.findById(requester.getId())
                    .orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    var addressee = createRandomUserIn2Dbs();
                    log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
                    userdataUserDao.addFriend(
                            requesterEntity,
                            addressee);
                    return null;
                });
            }

        }

    }

    @Override
    public void remove(UserModel userModel) {
        log.info("Remove user: {}", userModel);
        txTemplate.execute(status -> {
            new UserdataUserDaoSpringJdbc()
                    .remove(userMapper.toEntity(userModel));
            return null;
        });
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
