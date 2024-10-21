package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.rowMapper.AuthUserRowMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

@Slf4j
public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {

        try (
                PreparedStatement userPs = holder(AUTH_JDBC_URL).connection().prepareStatement(
                        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                                "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

                PreparedStatement authorityPs = holder(AUTH_JDBC_URL).connection().prepareStatement(
                        "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")
        ) {

            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getPassword());
            userPs.setBoolean(3, user.getEnabled());
            userPs.setBoolean(4, user.getAccountNonExpired());
            userPs.setBoolean(5, user.getAccountNonLocked());
            userPs.setBoolean(6, user.getCredentialsNonExpired());

            userPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);

            for (AuthAuthorityEntity a : user.getAuthorities()) {
                authorityPs.setObject(1, generatedKey);
                authorityPs.setString(2, a.getAuthority().name());
                authorityPs.addBatch();
                authorityPs.clearParameters();
            }
            authorityPs.executeBatch();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {

        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT u.id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, a.id AS a_id, a.user_id, a.authority  FROM \"user\" u JOIN authority a ON u.id = a.user_id WHERE u.id = ?")
        ) {

            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {

                AuthUserEntity user = null;
                List<AuthAuthorityEntity> authorityEntities = new ArrayList<>();

                while (rs.next()) {

                    if (user == null) {
                        user = AuthUserRowMapper.INSTANCE.mapRow(rs, 1);
                    }

                    AuthAuthorityEntity ae = AuthAuthorityEntity.builder()
                            .user(user)
                            .id(rs.getObject("a_id", UUID.class))
                            .authority(Authority.valueOf(rs.getString("authority")))
                            .build();
                    authorityEntities.add(ae);

                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorityEntities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {

        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT u.id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, a.id AS a_id, a.user_id, a.authority  FROM \"user\" u JOIN authority a ON u.id = a.user_id WHERE u.username = ?")
        ) {
            ps.setObject(1, username);

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthAuthorityEntity> authorityEntities = new ArrayList<>();

                while (rs.next()) {

                    if (user == null) {
                        user = AuthUserRowMapper.INSTANCE.mapRow(rs, 1);
                    }

                    AuthAuthorityEntity ae = AuthAuthorityEntity.builder()
                            .user(user)
                            .id(rs.getObject("a_id", UUID.class))
                            .authority(Authority.valueOf(rs.getString("authority")))
                            .build();
                    authorityEntities.add(ae);

                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorityEntities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<AuthUserEntity> findAll() {

        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT u.id, u.username, u.password, u.enabled, u.account_non_expired, u.account_non_locked, u.credentials_non_expired, a.id AS a_id, a.user_id, a.authority  FROM \"user\" u JOIN authority a ON u.id = a.user_id")
        ) {

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {

                List<AuthUserEntity> users = new ArrayList<>();
                while (rs.next()) {

                    AuthUserEntity user = AuthUserRowMapper.INSTANCE.mapRow(rs, 1);
                    List<AuthAuthorityEntity> authorityEntities = new ArrayList<>();

                    AuthAuthorityEntity ae = AuthAuthorityEntity.builder()
                            .user(user)
                            .id(rs.getObject("a_id", UUID.class))
                            .authority(Authority.valueOf(rs.getString("authority")))
                            .build();
                    authorityEntities.add(ae);

                }

                return users;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(AuthUserEntity user) {

        try (PreparedStatement authorityPs = holder(AUTH_JDBC_URL).connection()
                .prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?");

             PreparedStatement userPs = holder(AUTH_JDBC_URL).connection()
                     .prepareStatement("DELETE FROM \"user\" WHERE id = ?")
        ) {

            authorityPs.setObject(1, user.getId());
            authorityPs.executeUpdate();

            userPs.setObject(1, user.getId());
            userPs.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
