package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity create(AuthUserEntity userAuth) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO public.user( username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        " VALUES (?, ?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, userAuth.getUsername());
            ps.setString(2, userAuth.getPassword());
            ps.setBoolean(3, userAuth.isEnabled());
            ps.setBoolean(4, userAuth.isAccountNonExpired());
            ps.setBoolean(5, userAuth.isAccountNonLocked());
            ps.setBoolean(6, userAuth.isCredentialsNonExpired());

            ps.executeUpdate();
            final UUID generationKey;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generationKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            userAuth.setId(generationKey);
            return userAuth;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity update(AuthUserEntity authUser) {
        try(PreparedStatement ps = connection.prepareStatement(
                "UPDATE public.user SET password=? "
                +"WHERE id=?"
        )) {
            ps.setString(1, authUser.getPassword());
            ps.setObject(2, authUser.getId());
            ps.executeUpdate();
            return authUser;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM public.user WHERE id=?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try(ResultSet rs = ps.getResultSet()) {
                if(rs.next()) {
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
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user WHERE username=?"
        )) {
            ps.setObject(1, username);
            ps.execute();

            try(ResultSet rs = ps.getResultSet()) {
                if(rs.next()) {
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
        try(PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user"
        )) {
            ps.execute();
            try(ResultSet rs = ps.getResultSet()) {
                List<AuthUserEntity> list = new ArrayList<>();
                if(rs.next()){
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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public void delete(AuthUserEntity authUser) {
        try(PreparedStatement ps = connection.prepareStatement(
                "DELETE public.user WHERE id=? "
        )) {
            ps.setObject(1, authUser.getId());
            int resExecuteUpdate = ps.executeUpdate();
            if(resExecuteUpdate == 0){
                throw new SQLException("Can't find deleted user");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
