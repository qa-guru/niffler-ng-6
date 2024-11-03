package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDaoJdbc implements UserdataUserDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public UserdataUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO public.\"user\" (username, currency, firstname, surname, full_name, photo, photo_small)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().name());
                ps.setString(3, user.getFirstname());
                ps.setString(4, user.getSurname());
                ps.setString(5, user.getFullname());
                ps.setBytes(6, user.getPhoto());
                ps.setBytes(7, user.getPhotoSmall());

                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in the ResultsSet");
                    }
                }
                user.setId(generatedKey);
                return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM public.\"user\" WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity ue = new UserEntity();
                        ue.setId(rs.getObject("id", UUID.class));
                        ue.setUsername(rs.getString("username"));
                        ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        ue.setFirstname(rs.getString("firstname"));
                        ue.setSurname(rs.getString("surname"));
                        ue.setFullname(rs.getString("full_name"));
                        ue.setPhoto(rs.getBytes("photo"));
                        ue.setPhotoSmall(rs.getBytes("photo_small"));
                        return Optional.of(ue);
                    } else {
                        return Optional.empty();
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM public.\"user\" WHERE username = ?"
            )) {
                ps.setObject(1, username);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity ue = new UserEntity();
                        ue.setId(rs.getObject("id", UUID.class));
                        ue.setUsername(rs.getString("username"));
                        ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        ue.setFirstname(rs.getString("firstname"));
                        ue.setSurname(rs.getString("surname"));
                        ue.setFullname(rs.getString("full_name"));
                        ue.setPhoto(rs.getBytes("photo"));
                        ue.setPhotoSmall(rs.getBytes("photo_small"));
                        return Optional.of(ue);
                    } else {
                        return Optional.empty();
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM public.\"user\" WHERE id = ?"
            )) {
                ps.setObject(1, user.getId());
                ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
