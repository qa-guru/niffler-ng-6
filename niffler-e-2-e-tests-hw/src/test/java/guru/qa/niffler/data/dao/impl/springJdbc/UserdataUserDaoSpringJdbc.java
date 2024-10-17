package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.rowMapper.UserdataUserRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDaoSpringJdbc implements UserdataUserDao {

    private final DataSource dataSource;

    public UserdataUserDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                                    "VALUES (?,?,?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstName());
                    ps.setString(4, user.getSurname());
                    ps.setBytes(5, user.getPhoto());
                    ps.setBytes(6, user.getPhotoSmall());
                    ps.setString(7, user.getFullName());
                    return ps;
                },
                keyHolder
        );

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);
        return user;

    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UserdataUserRowMapper.INSTANCE,
                        id
                ));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            // QueryForObject not returns null if not found object. Method throws EmptyResultDataAccessException
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE username = ?",
                            UserdataUserRowMapper.INSTANCE,
                            username
                    ));
        } catch (
                EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                UserdataUserRowMapper.INSTANCE
        );
    }

    @Override
    public void delete(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(
                "DELETE FROM \"user\" WHERE id = ?",
                user.getId()
        );
    }

}
