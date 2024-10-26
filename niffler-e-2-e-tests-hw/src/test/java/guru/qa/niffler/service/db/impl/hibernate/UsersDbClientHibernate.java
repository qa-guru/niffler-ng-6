package guru.qa.niffler.service.db.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.mapper.UserMapper;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.db.UsersDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UsersDbClientHibernate implements UsersDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private final AuthUserMapper authUserMapper = new AuthUserMapper();
    private final UserMapper userMapper = new UserMapper();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL, USERDATA_JDBC_URL);


    @Override
    public UserModel createUser(UserModel userModel) {

        log.info("Creating new user with authorities in niffler-auth and niffler-userdata by DTO: {}", userModel);

        var authUserEntity = authUserMapper.toEntity(userMapper.toAuthDto(userModel));
        authUserEntity.setAuthorities(
                List.of(AuthAuthorityEntity.builder().authority(Authority.read).user(authUserEntity).build(),
                        AuthAuthorityEntity.builder().authority(Authority.write).user(authUserEntity).build())
        );

        return xaTxTemplate.execute(() -> {
            authUserRepository.create(authUserEntity);
            return userMapper.toDto(
                    userdataUserRepository.create(
                            userMapper.toEntity(userModel)));

        });


    }

    @Override
    public void getIncomeInvitationFromNewUsers(UserModel requester, int count) {

        if (count > 0) {
            UserEntity requesterEntity = userdataUserRepository.findById(
                    requester.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
                            userdataUserRepository.sendInvitation(
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
            UserEntity requesterEntity = userdataUserRepository.findById(
                    requester.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Create invitation from [{}] to [{}] with status", requester.getUsername(), addressee.getUsername());
                            userdataUserRepository.sendInvitation(
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
    public void addNewFriends(UserModel requester, int count) {

        if (count > 0) {
            UserEntity requesterEntity = userdataUserRepository.findById(
                    requester.getId()
            ).orElseThrow(() -> new UserNotFoundException("User with id = [" + requester.getId() + "] not found"));

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            var addressee = createRandomUserIn2Dbs();
                            log.info("Make users are friends: [{}], [{}]", requester.getUsername(), addressee.getUsername());
                            userdataUserRepository.addFriend(
                                    requesterEntity,
                                    addressee);
                            return null;
                        }
                );
            }
        }
    }

    @Override
    public void removeUser(UserModel userModel) {
        log.info("Remove user from niffler-auth and niffler-userdata with username = [{}]", userModel.getUsername());
        xaTxTemplate.execute(() -> {
            authUserRepository.findByUsername(userModel.getUsername())
                    .ifPresent(authUserRepository::remove);
            userdataUserRepository.findByUsername(userModel.getUsername())
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
