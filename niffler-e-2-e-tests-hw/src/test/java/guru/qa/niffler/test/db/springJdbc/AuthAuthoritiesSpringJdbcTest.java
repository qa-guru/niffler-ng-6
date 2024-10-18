package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.service.impl.springJdbc.AuthAuthorityDbClientSpringJdbc;
import guru.qa.niffler.service.impl.springJdbc.AuthUserDbClientSpringJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static guru.qa.niffler.utils.UserUtils.generateAuthUser;

@Slf4j
class AuthAuthoritiesSpringJdbcTest {

    @Test
    void shouldCreateNewAuthoritiesAndFindByUsernameTest() {

        var authorityDbClient = new AuthAuthorityDbClientSpringJdbc();
        var authUser = new AuthUserDbClientSpringJdbc()
                .create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .userId(authUser.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityJson[]::new));
        Assertions.assertEquals(2, authorityDbClient.findByUserId(authUser.getId()).size());

    }

    @Test
    void shouldFindAuthorityById() {

        var authorityDbClient = new AuthAuthorityDbClientSpringJdbc();
        var authUser = new AuthUserDbClientSpringJdbc()
                .create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .userId(authUser.getId())
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
    void shouldFindAll() {

        var authorityDbClient = new AuthAuthorityDbClientSpringJdbc();
        var authUser = new AuthUserDbClientSpringJdbc()
                .create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .userId(authUser.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityJson[]::new));

        Assertions.assertFalse(authorityDbClient.findAll().isEmpty());

    }

    @Test
    void shouldDeleteAuthorities() {

        var authorityDbClient = new AuthAuthorityDbClientSpringJdbc();
        var authUser = new AuthUserDbClientSpringJdbc()
                .create(generateAuthUser());

        authorityDbClient.create(
                Arrays.stream(Authority.values())
                        .map(authority ->
                                AuthAuthorityJson.builder()
                                        .userId(authUser.getId())
                                        .authority(authority)
                                        .build())
                        .toArray(AuthAuthorityJson[]::new));

        authorityDbClient.delete(authorityDbClient
                .findByUserId(authUser.getId())
                .toArray(AuthAuthorityJson[]::new));

        Assertions.assertTrue(authorityDbClient.findByUserId(authUser.getId()).isEmpty());

    }

}
