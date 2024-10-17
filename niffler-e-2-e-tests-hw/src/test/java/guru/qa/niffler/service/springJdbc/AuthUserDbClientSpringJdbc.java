package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserJson;
import guru.qa.niffler.mapper.AuthUserMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;

@Slf4j
public class AuthUserDbClientSpringJdbc {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final AuthUserMapper authUserMapper = new AuthUserMapper();

    public AuthUserJson create(AuthUserJson authUserJson) {
        log.info("Creating new user by DTO: {}", authUserJson);
        return authUserMapper.toDto(
                new AuthUserDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                        .create(authUserMapper.toEntity(authUserJson)));
    }

    public Optional<AuthUserJson> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return new AuthUserDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .findById(id)
                .map(authUserMapper::toDto);
    }

    public Optional<AuthUserJson> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return new AuthUserDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .findByUsername(username)
                .map(authUserMapper::toDto);
    }

    public List<AuthUserJson> findAll() {
        log.info("Get all auth users");
        return new AuthUserDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .findAll().stream()
                .map(authUserMapper::toDto)
                .toList();
    }

    public void delete(AuthUserJson authUser) {
        log.info("Remove user: {}", authUser);
        new AuthUserDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .delete(authUserMapper.toEntity(authUser));
    }

}
