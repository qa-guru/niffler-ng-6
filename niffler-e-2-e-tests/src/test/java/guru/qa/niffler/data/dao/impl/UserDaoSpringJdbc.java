package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.data.mapper.UserEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDaoSpringJdbc implements UserDao {

    private final DataSource dataSource;

    public UserDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO public.user (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ? ,?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            return ps;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM public.user WHERE id = ?",
                        UserEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM public.user WHERE id = ?",
                        UserEntityRowMapper.instance,
                        username
                )
        );
    }

    @Override
    public List<UserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query(
                "SELECT * FROM public.user",
                UserEntityRowMapper.instance
        );
    }

    @Override
    public void delete(UserEntity user) {

    }
}
