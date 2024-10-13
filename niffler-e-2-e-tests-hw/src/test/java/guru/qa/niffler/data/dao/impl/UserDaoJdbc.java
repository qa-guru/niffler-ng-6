package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {

    private final String userdataJdbcUrl = Config.getInstance().userdataJdbcUrl();

    public UserEntity create(UserEntity user) {

        try (Connection connection = Databases.connection(userdataJdbcUrl)) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO public.user (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().name());
                ps.setString(3, user.getFirstName());
                ps.setString(4, user.getSurname());
                ps.setBytes(5, user.getPhoto());
                ps.setBytes(6, user.getPhotoSmall());
                ps.setString(7, user.getFullName());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return fromResultSet(rs);
                    } else {
                        throw new SQLException("Could not find 'id' in ResultSet");
                    }
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<UserEntity> findById(UUID id) {

        try (Connection connection = Databases.connection(userdataJdbcUrl)) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM public.user WHERE id = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setObject(1, id);
                ps.executeQuery();

                try (ResultSet rs = ps.getResultSet()) {
                    return rs.next()
                            ? Optional.of(fromResultSet(rs))
                            : Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {

        try (Connection connection = Databases.connection(userdataJdbcUrl)) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM public.user WHERE username = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, username);
                ps.executeQuery();

                try (ResultSet rs = ps.getResultSet()) {
                    return rs.next()
                            ? Optional.of(fromResultSet(rs))
                            : Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(UserEntity user) {

        try (Connection connection = Databases.connection(userdataJdbcUrl)) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM public.user WHERE id = ?"
            )) {
                ps.setObject(1, user.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private UserEntity fromResultSet(ResultSet rs) throws SQLException {
        return UserEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .currency(CurrencyValues.valueOf(rs.getString("currency")))
                .firstName(rs.getString("firstname"))
                .surname(rs.getString("surname"))
                .photo(rs.getBytes("photo"))
                .photoSmall(rs.getBytes("photo_small"))
                .fullName(rs.getString("full_name"))
                .build();
    }

}
