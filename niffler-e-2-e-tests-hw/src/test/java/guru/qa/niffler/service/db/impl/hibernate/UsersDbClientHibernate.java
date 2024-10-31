package guru.qa.niffler.service.db.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.db.UsersDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SuppressWarnings("unchecked")
public class UsersDbClientHibernate implements UsersDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private final AuthUserMapper authUserMapper = new AuthUserMapper();
    private final UserMapper userMapper = new UserMapper();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL, USERDATA_JDBC_URL);


    @Override
    public UserJson createUser(@Nonnull UserJson userJson) {

        log.info("Creating new user with authorities in niffler-auth and niffler-userdata by DTO: {}", userJson);

        var userPassword = userJson.getPassword();
        var authUserEntity = authUserMapper.toEntity(userMapper.toAuthDto(userJson));
        authUserEntity.setAuthorities(
                List.of(AuthAuthorityEntity.builder().authority(Authority.read).user(authUserEntity).build(),
                        AuthAuthorityEntity.builder().authority(Authority.write).user(authUserEntity).build())
        );

        return xaTxTemplate.execute(() -> {
                    authUserRepository.create(authUserEntity);
                    return userMapper.toDto(
                            userdataUserRepository.create(
                                    userMapper.toEntity(userJson)));

                })
                .setPassword(userPassword);


    }

    @Override
    public List<UserJson> getIncomeInvitationFromNewUsers(@Nonnull UserJson to, int count) {

        List<UserJson> users = new ArrayList<>();
        if (count > 0) {
            UserEntity toUserEntity = userdataUserRepository.findById(
                    to.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + to.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    var fromUserEntity = createRandomUserIn2Dbs();
                    users.add(userMapper.toDto(fromUserEntity));
                    log.info("Create invitation from [{}] to [{}] with status PENDING", toUserEntity.getUsername(), fromUserEntity.getUsername());
                    userdataUserRepository.sendInvitation(
                            fromUserEntity,
                            toUserEntity);
                    return null;
                });
            }
        }
        return users;
    }

    @Override
    public List<UserJson> sendOutcomeInvitationToNewUsers(@Nonnull UserJson from, int count) {

        List<UserJson> users = new ArrayList<>();
        if (count > 0) {
            UserEntity fromEntity = userdataUserRepository.findById(
                    from.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + from.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    var toEntity = createRandomUserIn2Dbs();
                    users.add(userMapper.toDto(toEntity));
                    log.info("Create invitation from [{}] to [{}] with status", fromEntity.getUsername(), toEntity.getUsername());
                    userdataUserRepository.sendInvitation(
                            fromEntity,
                            toEntity);
                    return null;
                });
            }
        }
        return users;
    }

    @Override
    public List<UserJson> addNewFriends(@Nonnull UserJson userJson, int count) {

        List<UserJson> users = new ArrayList<>();
        if (count > 0) {
            UserEntity fromEntity = userdataUserRepository.findById(
                    userJson.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + userJson.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                    var toEntity = createRandomUserIn2Dbs();
                    users.add(userMapper.toDto(toEntity));
                    log.info("Make users are friends: [{}], [{}]", fromEntity.getUsername(), toEntity.getUsername());
                    userdataUserRepository.addFriend(
                            fromEntity,
                            toEntity);
                    return null;
                });
            }
        }
        return users;
    }

    @Override
    public void removeUser(@Nonnull UserJson userJson) {
        log.info("Remove user from niffler-auth and niffler-userdata with username = [{}]", userJson.getUsername());
        xaTxTemplate.execute(() -> {
            authUserRepository.findByUsername(userJson.getUsername())
                    .ifPresent(authUserRepository::remove);
            userdataUserRepository.findByUsername(userJson.getUsername())
                    .ifPresent(userdataUserRepository::remove);
            return null;
        });
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
        return userdataUserRepository.create(userMapper.toEntity(generatedUser));

    }

}
