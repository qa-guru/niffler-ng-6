package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.model.AuthAuthorityJson;
import guru.qa.niffler.service.AuthAuthorityDbClient;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.impl.jdbc.AuthAuthorityDbClientJdbc;
import guru.qa.niffler.service.impl.jdbc.AuthUserDbClientJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static guru.qa.niffler.utils.UserUtils.generateAuthUser;

@Slf4j
class AuthAuthoritiesJdbcTest {

    private final AuthAuthorityDbClient authorityDbClient = new AuthAuthorityDbClientJdbc();
    private final AuthUserDbClient authUserDbClient = new AuthUserDbClientJdbc();

    @Test
    void shouldCreateNewAuthoritiesAndFindByUsernameTest() {

        var authUser = authUserDbClient.create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .user(authUser.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityJson[]::new));
        Assertions.assertEquals(2, authorityDbClient.findByUserId(authUser.getId()).size());

    }

    @Test
    void shouldFindAuthorityByIdTest() {

        var authUser = authUserDbClient.create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .user(authUser.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityJson[]::new));

        Assertions.assertNotNull(
                authorityDbClient.findById(
                        authorityDbClient
                                .findByUserId(authUser.getId())
                                .getFirst()
                                .getId()
                ));

    }

    @Test
    void shouldFindAllTest() {

        var authUser = authUserDbClient.create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .user(authUser.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityJson[]::new));

        Assertions.assertFalse(authorityDbClient.findAll().isEmpty());

    }

    @Test
    void shouldDeleteAuthoritiesTest() {

        var authUser = authUserDbClient.create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .user(authUser.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityJson[]::new));

        authorityDbClient.delete(authorityDbClient
                .findByUserId(authUser.getId())
                .toArray(AuthAuthorityJson[]::new));

        Assertions.assertTrue(authorityDbClient.findByUserId(authUser.getId()).isEmpty());

    }

}
