package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepsitoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity create(AuthUserEntity userAuth) {
        try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO public.user( username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        " VALUES (?, ?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS);
             PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO public.authority(user_id, authority) " +
                             "VALUES (?, ?);")
        ) {
            userPs.setString(1, userAuth.getUsername());
            userPs.setString(2, userAuth.getPassword());
            userPs.setBoolean(3, userAuth.getEnabled());
            userPs.setBoolean(4, userAuth.getAccountNonExpired());
            userPs.setBoolean(5, userAuth.getAccountNonLocked());
            userPs.setBoolean(6, userAuth.getCredentialsNonExpired());

            userPs.executeUpdate();
            final UUID generationKey;

            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generationKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            userAuth.setId(generationKey);

            for (AuthAuthorityEntity a : userAuth.getAuthorities()) {
                authorityPs.setObject(1, generationKey);
                authorityPs.setString(2, a.getAuthority().toString());
                authorityPs.addBatch();
                authorityPs.clearParameters();
            }
            authorityPs.executeBatch();
            return userAuth;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity update(AuthUserEntity authUser) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "UPDATE public.user SET password=? "
                        + "WHERE id=?"
        )) {
            ps.setString(1, authUser.getPassword());
            ps.setObject(2, authUser.getId());
            ps.executeUpdate();
            return authUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM public.user u JOIN public.authority a ON u.id = a.user_id WHERE id=?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthAuthorityEntity> list = new ArrayList<>();
                while (rs.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
                    }
                    AuthAuthorityEntity authority = new AuthAuthorityEntity();
                    authority.setId(rs.getObject("a.id", UUID.class));
                    authority.setUser(user);
                    authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                    list.add(authority);
                    AuthUserEntity aue = new AuthUserEntity();
                    aue.setId(rs.getObject("id", UUID.class));
                    aue.setUsername(rs.getString("username"));
                    aue.setPassword(rs.getString("password"));
                    aue.setEnabled(rs.getBoolean("enabled"));
                    aue.setAccountNonLocked(rs.getBoolean("account_non_expired"));
                    aue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    aue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                }
                if(user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(list);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM public.user WHERE username=?"
        )) {
            ps.setObject(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity aue = new AuthUserEntity();
                    aue.setId(rs.getObject("id", UUID.class));
                    aue.setUsername(rs.getString("username"));
                    aue.setPassword(rs.getString("password"));
                    aue.setEnabled(rs.getBoolean("enabled"));
                    aue.setAccountNonLocked(rs.getBoolean("account_non_expired"));
                    aue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    aue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return Optional.of(aue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM public.user"
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                List<AuthUserEntity> list = new ArrayList<>();
                if (rs.next()) {
                    AuthUserEntity aue = new AuthUserEntity();
                    aue.setId(rs.getObject("id", UUID.class));
                    aue.setUsername(rs.getString("username"));
                    aue.setPassword(rs.getString("password"));
                    aue.setEnabled(rs.getBoolean("enabled"));
                    aue.setAccountNonLocked(rs.getBoolean("account_non_expired"));
                    aue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    aue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    list.add(aue);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void delete(AuthUserEntity authUser) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE public.user WHERE id=? "
        )) {
            ps.setObject(1, authUser.getId());
            int resExecuteUpdate = ps.executeUpdate();
            if (resExecuteUpdate == 0) {
                throw new SQLException("Can't find deleted user");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
