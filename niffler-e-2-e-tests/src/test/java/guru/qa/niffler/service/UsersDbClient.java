package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UdUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UdUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import jaxb.userdata.FriendState;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.Arrays;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UdUserRepository udUserRepository = new UdUserRepositoryJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUserWithFriend(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            // Сначала создаем основного пользователя в базе niffler-auth
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345")); // Задаем пароль
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(
                    Arrays.stream(Authority.values()).map(e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUser(authUser);
                        ae.setAuthority(e);
                        return ae;
                    }).toList()
            );
            authUserRepository.create(authUser); // Сохраняем пользователя в niffler-auth

            // Преобразуем основной объект UserJson в сущность для работы с базой niffler-userdata
            UserEntity udUser = UserEntity.fromJson(user);

            // Сохраняем основного пользователя в базе niffler-userdata, чтобы получить его id
            udUserRepository.create(udUser);

            UserEntity udFriendUser = null;

            // Проверяем наличие статуса дружбы и необходимость создания второго пользователя (друга)
            if (user.friendState() != null && user.friendState() != FriendState.VOID) {
                // Генерируем случайное имя для второго пользователя (друга)
                String randomUsername = RandomDataUtils.randomUsername();
                AuthUserEntity friendAuthUser = new AuthUserEntity();
                friendAuthUser.setUsername(randomUsername);
                friendAuthUser.setPassword(pe.encode("12345")); // Задаем пароль для друга
                friendAuthUser.setEnabled(true);
                friendAuthUser.setAccountNonExpired(true);
                friendAuthUser.setAccountNonLocked(true);
                friendAuthUser.setCredentialsNonExpired(true);
                friendAuthUser.setAuthorities(
                        Arrays.stream(Authority.values()).map(e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(friendAuthUser);
                            ae.setAuthority(e);
                            return ae;
                        }).toList()
                );
                // Сохраняем друга в niffler-auth
                authUserRepository.create(friendAuthUser);

                // Преобразуем друга в сущность для работы с базой niffler-userdata
                UserJson friendUserJson = new UserJson(
                        null,
                        randomUsername,
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                );

                udFriendUser = UserEntity.fromJson(friendUserJson);
                udUserRepository.create(udFriendUser); // Сохраняем друга в niffler-userdata

                // Добавляем информацию о статусе дружбы между основным пользователем и другом
                switch (user.friendState()) {
                    case INVITE_RECEIVED -> {
                        udUser.addInvitations(udFriendUser); // Основной пользователь получил приглашение
                        udFriendUser.addFriends(FriendshipStatus.PENDING, udUser); // Друг отправил приглашение
                    }
                    case INVITE_SENT -> {
                        udUser.addFriends(FriendshipStatus.PENDING, udFriendUser); // Основной пользователь отправил приглашение
                        udFriendUser.addInvitations(udUser); // Друг получил приглашение
                    }
                    case FRIEND -> {
                        udUser.addFriends(FriendshipStatus.ACCEPTED, udFriendUser); // Дружба подтверждена
                        udFriendUser.addFriends(FriendshipStatus.ACCEPTED, udUser); // Дружба подтверждена
                    }
                }

                // Сохраняем информацию о дружбе для обоих пользователей в базе niffler-userdata
                udUserRepository.addFriendships(udUser);
                udUserRepository.addFriendships(udFriendUser);
            }

            // Возвращаем информацию о созданном пользователе и статусе дружбы (если есть)
            return UserJson.fromEntity(udUser, user.friendState());
        });
    }
}