package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.extractor.AuthUserResultSetExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, " +
                            "account_non_locked, credentials_non_expired) VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            return ps;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);

        String insertAuthoritySql = "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(insertAuthoritySql, user.getAuthorities(), user.getAuthorities().size(),
                (ps, authority) -> {
                    ps.setObject(1, generatedKey);
                    ps.setString(2, authority.getAuthority().name());
                });

        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        String updateUserSql = "UPDATE \"user\" SET password = ?, enabled = ?, " +
                "account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? " +
                "WHERE id = ?";

        String clearAuthoritySql = "DELETE FROM \"authority\" WHERE user_id = ?";

        String insertAuthoritySql = "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)";

        jdbcTemplate.update(
                updateUserSql,
                user.getPassword(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getAccountNonLocked(),
                user.getCredentialsNonExpired(),
                user.getId()
        );

        jdbcTemplate.update(clearAuthoritySql, user.getId());

        jdbcTemplate.batchUpdate(insertAuthoritySql, user.getAuthorities(), user.getAuthorities().size(),
                (ps, authority) -> {
                    ps.setObject(1, user.getId());
                    ps.setString(2, authority.getAuthority().name());
                });
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        String sql = "SELECT a.id as authority_id, a.authority, u.id as user_id, u.username, u.password, " +
                "u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired " +
                "FROM \"user\" u " +
                "JOIN authority a ON u.id = a.user_id " +
                "WHERE u.id = ?";

        Map<UUID, AuthUserEntity> userMap = jdbcTemplate.query(
                sql,
                new AuthUserResultSetExtractor(),
                id
        );

        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String userName) {
        String sql = "SELECT a.id as authority_id, a.authority, u.id as user_id, u.username, u.password, " +
                "u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired " +
                "FROM \"user\" u " +
                "JOIN authority a ON u.id = a.user_id " +
                "WHERE u.username = ?";

        Map<UUID, AuthUserEntity> userMap = jdbcTemplate.query(
                sql,
                new AuthUserResultSetExtractor(),
                userName
        );

        return userMap.values().stream().findFirst();
    }

    @Override
    public void remove(AuthUserEntity user) {
        String deleteAuthoritySql = "DELETE FROM \"authority\" WHERE user_id = ?";
        jdbcTemplate.update(deleteAuthoritySql, user.getId());

        String deleteUserSql = "DELETE FROM \"user\" WHERE id = ?";
        jdbcTemplate.update(deleteUserSql, user.getId());
    }
}
