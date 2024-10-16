package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.service.jdbc.AuthAuthorityDbClient;
import guru.qa.niffler.service.jdbc.AuthUserDbClient;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class AuthAuthoritiesJdbcTest {

    AuthAuthorityDbClient authAuthorityDbClient = new AuthAuthorityDbClient();

    @Test
    void shouldCreateNewAuthoritiesAndFindByUsernameTest() {

        var user = new AuthUserDbClient().create(UserUtils.generateAuthUser());
        authAuthorityDbClient.create(
                AuthAuthorityJson.builder()
                        .userId(user.getId())
                        .authority(Authority.read)
                        .build(),
                AuthAuthorityJson.builder()
                        .userId(user.getId())
                        .authority(Authority.write)
                        .build()
        );

        var authorities = authAuthorityDbClient.findByUserId(user.getId());
        assertEquals(2, authorities.size());

    }

    @Test
    void shouldFindAuthorityById() {
        var user = new AuthUserDbClient().create(UserUtils.generateAuthUser());
        authAuthorityDbClient.create(
                AuthAuthorityJson.builder()
                        .userId(user.getId())
                        .authority(Authority.read)
                        .build(),
                AuthAuthorityJson.builder()
                        .userId(user.getId())
                        .authority(Authority.write)
                        .build()
        );
        var authorities = authAuthorityDbClient.findByUserId(user.getId());
        assertNotNull(authAuthorityDbClient.findById(authorities.getFirst().getId()).orElse(null));
    }

    @Test
    void shouldDeleteAuthorities() {
        var user = new AuthUserDbClient().create(UserUtils.generateAuthUser());
        authAuthorityDbClient.create(
                AuthAuthorityJson.builder()
                        .userId(user.getId())
                        .authority(Authority.read)
                        .build(),
                AuthAuthorityJson.builder()
                        .userId(user.getId())
                        .authority(Authority.write)
                        .build()
        );
        var authorities = authAuthorityDbClient.findByUserId(user.getId());
        authAuthorityDbClient.delete(authorities.toArray(new AuthAuthorityJson[0]));
        authorities = authAuthorityDbClient.findByUserId(user.getId());
        log.info("Authorities: {}", authorities.toString());
//        assertTrue();
    }

}
