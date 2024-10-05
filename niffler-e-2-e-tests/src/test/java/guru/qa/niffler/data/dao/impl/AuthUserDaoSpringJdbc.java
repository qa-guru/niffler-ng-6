package guru.qa.niffler.data.dao.impl;


import guru.qa.niffler.data.dao.AuthUserDao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private final DataSource dataSource;

    public AuthUserDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO public.user( username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                            " VALUES (?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, authUser.getUsername());
            ps.setString(2, authUser.getPassword());
            ps.setBoolean(3, authUser.isEnabled());
            ps.setBoolean(4, authUser.isAccountNonExpired());
            ps.setBoolean(5, authUser.isAccountNonLocked());
            ps.setBoolean(6, authUser.isCredentialsNonExpired());
            return ps;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        authUser.setId(generatedKey);
        return authUser;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity authUser) {
        return null;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM public.user WHERE id = ?",
                        AuthUserEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM public.user WHERE username = ?",
                        AuthUserEntityRowMapper.instance,
                        username
                )
        );
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query(
                "SELECT * FROM public.user",
                AuthUserEntityRowMapper.instance
        );
    }

    @Override
    public void delete(AuthUserEntity authUser) {
        new JdbcTemplate().update(
                "DELETE public.user WHERE id = ? ",
                authUser.getId()
        );
    }
}
