package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.rowMapper.AuthUserRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                                    "VALUES (?,?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, user.getUsername());
                    ps.setString(2, PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword()));
                    ps.setBoolean(3, user.isEnabled());
                    ps.setBoolean(4, user.isAccountNonExpired());
                    ps.setBoolean(5, user.isAccountNonLocked());
                    ps.setBoolean(6, user.isCredentialsNonExpired());
                    return ps;
                },
                keyHolder
        );

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);
        return user;

    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        try {
            // QueryForObject not returns null if not found object. Method throws EmptyResultDataAccessException
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE id = ?",
                            AuthUserRowMapper.INSTANCE,
                            id
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        try {
            // QueryForObject not returns null if not found object. Method throws EmptyResultDataAccessException
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE username = ?",
                            AuthUserRowMapper.INSTANCE,
                            username
                    )
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                AuthUserRowMapper.INSTANCE
        );
    }

    @Override
    public void delete(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        jdbcTemplate.update(
                "DELETE FROM \"user\" WHERE id = ?",
                user.getId()
        );
    }

}
