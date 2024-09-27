package guru.qa.niffler.test.web;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.AuthorityJson;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@WebTest
//Прошу это не проверять. Написано для себя, скорее всего удалю чуть позже
public class CheckDbTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    AuthUserDbClient authUserDbClient = new AuthUserDbClient(passwordEncoder);

    @Test
    void createSpendTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(null,
                                RandomDataUtils.randomCategoryName(),
                                "missa",
                                false),
                        CurrencyValues.RUB,
                        100.0,
                        "test desc",
                        "missa"
                )
        );
        System.out.println(spend);
    }

    @Test
    void createUserTest() {
        UserDbClient userDbClient = new UserDbClient();
        UserEntity user = new UserEntity();
        String userName = RandomDataUtils.randomUsername();
        user.setUsername(userName);
        user.setFirstname("Test");
        user.setSurname("User");
        user.setFullname("Test User");
        user.setCurrency(CurrencyValues.USD);

        UserEntity createdUser = userDbClient.createUser(user);

        assertEquals(userName, createdUser.getUsername(), "Expected username to match");
    }

    @Test
    void findUserByIdTest() {
        UserDbClient userDbClient = new UserDbClient();
        UUID userId = UUID.fromString("2af3b94a-222e-4499-ac04-d4aa4047c0f0");

        Optional<UserEntity> foundUser = userDbClient.findUserById(userId);

        System.out.println(foundUser.get().getUsername());
        assertTrue(foundUser.isPresent(), "Expected to find a user with given ID");
        assertEquals(userId, foundUser.get().getId(), "Expected user ID to match");
    }

    @Test
    void findUserByUsernameTest() {
        UserDbClient userDbClient = new UserDbClient();
        String username = "vito.satterfield";

        Optional<UserEntity> foundUser = userDbClient.findUserByUsername(username);

        System.out.println(foundUser.get().getId());
        assertTrue(foundUser.isPresent(), "Expected to find a user with given username");
        assertEquals(username, foundUser.get().getUsername(), "Expected username to match");
    }

    @Test
    void deleteUserTest() {
        UserDbClient userDbClient = new UserDbClient();
        UserEntity user = new UserEntity();
        user.setUsername(RandomDataUtils.randomUsername());
        user.setFirstname("Delete");
        user.setSurname("User");
        user.setFullname("Delete User");
        user.setCurrency(CurrencyValues.EUR);

        UserEntity createdUser = userDbClient.createUser(user);
        userDbClient.deleteUser(createdUser);

        Optional<UserEntity> deletedUser = userDbClient.findUserById(createdUser.getId());
        assertFalse(deletedUser.isPresent(), "Expected user to be deleted");
    }

    @Test
    void createAuthTest() {
        AuthUserJson authUserJson = new AuthUserJson(
                null,
                "testUser7",
                "password123",
                true,
                true,
                true,
                true,
                List.of(new AuthorityJson(null, AuthorityEntity.Authority.READ.name()))
        );

        AuthUserJson createdUser = authUserDbClient.createAuth(authUserJson);

        // Проверяем, что пользователь создан корректно
        assertNotNull(createdUser);
        assertEquals("testUser7", createdUser.username());
        assertTrue(passwordEncoder.matches("password123", createdUser.password()));

        // Дополнительная проверка на наличие authority
        assertNotNull(createdUser.authorities());
        assertEquals(1, createdUser.authorities().size());
        assertEquals(AuthorityEntity.Authority.READ.name(), createdUser.authorities().get(0).authority());
    }

    @Test
    void createAuthTestJTAErrorTest() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AuthUserDbClient authUserDbClient = new AuthUserDbClient(passwordEncoder);

        AuthUserJson authUserJson = new AuthUserJson(
                null,
                "testUser10",
                "password123",
                true,
                true,
                true,
                true,
                List.of(new AuthorityJson(null, AuthorityEntity.Authority.READ.name()))
        );

        // Имитируем ошибку, установив username в null
        AuthUserJson invalidAuthUserJson = new AuthUserJson(
                authUserJson.id(),
                null, // Теперь username равен null
                authUserJson.password(),
                authUserJson.enabled(),
                authUserJson.accountNonExpired(),
                authUserJson.accountNonLocked(),
                authUserJson.credentialsNonExpired(),
                authUserJson.authorities()
        );

        assertThrows(RuntimeException.class, () -> {
            authUserDbClient.createAuth(invalidAuthUserJson);
        });
    }
}