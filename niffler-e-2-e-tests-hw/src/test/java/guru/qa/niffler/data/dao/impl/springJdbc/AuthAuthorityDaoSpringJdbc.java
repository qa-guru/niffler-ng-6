package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.rowMapper.AuthAuthorityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();

    @Override
    public void create(AuthAuthorityEntity... authority) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        jdbcTemplate.batchUpdate(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getUserId());
                        ps.setString(2, authority[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

    @Override
    public Optional<AuthAuthorityEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        try {
            // QueryForObject not returns null if not found object. Method throws EmptyResultDataAccessException
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"authority\" WHERE id = ?",
                            AuthAuthorityRowMapper.INSTANCE,
                            id)
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<AuthAuthorityEntity> findByUserId(UUID userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        return jdbcTemplate.query(
                "SELECT * FROM \"authority\" WHERE user_id = ?",
                AuthAuthorityRowMapper.INSTANCE,
                userId
        );
    }

    @Override
    public List<AuthAuthorityEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        return jdbcTemplate.query(
                "SELECT * FROM \"authority\"",
                AuthAuthorityRowMapper.INSTANCE
        );
    }

    @Override
    public void delete(AuthAuthorityEntity... authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(AUTH_JDBC_URL));
        jdbcTemplate.batchUpdate(
                "DELETE FROM \"authority\" WHERE id = ?",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority[i].getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }

}
