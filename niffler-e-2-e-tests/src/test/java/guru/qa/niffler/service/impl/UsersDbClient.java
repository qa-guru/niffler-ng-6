package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Nonnull
    @Override
    @Step("Создание нового пользователя: {username}")
    public UserJson createUser(String username, String password) {
        return Objects.requireNonNull(
                xaTransactionTemplate.execute(() -> UserJson.fromEntity(
                        createNewUser(username, password),
                        null
                ))
        );
    }

    @NotNull
    @Override
    @Step("Добавление {count} входящих приглашений пользователю: {targetUser.username}")
    public List<String> addIncomeInvitation(UserJson targetUser, int count) {
        List<String> incomes = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    userdataUserRepository.addFriendshipRequest(createNewUser(username, "12345"), targetEntity);
                    return null;
                });
                incomes.add(username);
            }
        }
        return incomes;
    }

    @NotNull
    @Override
    @Step("Добавление {count} исходящих приглашений пользователю: {targetUser.username}")
    public List<String> addOutcomeInvitation(UserJson targetUser, int count) {
        List<String> outcomes = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    userdataUserRepository.addFriendshipRequest(targetEntity, createNewUser(username, "12345"));
                    return null;
                });
                outcomes.add(username);
            }
        }
        return outcomes;
    }

    @NotNull
    @Override
    @Step("Добавление {count} друзей пользователю: {targetUser.username}")
    public List<String> addFriend(UserJson targetUser, int count) {
        List<String> friends = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    userdataUserRepository.addFriend(targetEntity, createNewUser(username, "12345"));
                    return null;
                });
                friends.add(username);
            }
        }
        return friends;
    }

    @Nonnull
    private UserEntity createNewUser(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepository.create(authUser);
        return userdataUserRepository.create(userEntity(username));
    }

    @Nonnull
    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    @Nonnull
    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}