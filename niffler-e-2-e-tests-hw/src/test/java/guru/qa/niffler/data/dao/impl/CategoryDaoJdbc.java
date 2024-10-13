package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();

    public CategoryEntity create(CategoryEntity category) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO category (username, name, archived) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, category.getUsername());
                ps.setString(2, category.getName());
                ps.setBoolean(3, category.isArchived());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return fromResultSet(rs);
                    } else {
                        throw new SQLException("Could find 'id' in ResultSet");
                    }
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<CategoryEntity> findById(UUID id) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM category WHERE id = ?",
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
    public Optional<CategoryEntity> findByUsernameAndName(String username, String categoryName) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM category WHERE username = ? AND name = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, username);
                ps.setString(2, categoryName);
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
    public List<CategoryEntity> findAllByUsername(String username) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM category WHERE username = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, username);
                ps.executeQuery();

                try (ResultSet rs = ps.getResultSet()) {
                    List<CategoryEntity> categories = new ArrayList<>();
                    while (rs.next())
                        categories.add(fromResultSet(rs));
                    return categories;

                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(CategoryEntity category) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM category WHERE id = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {
                ps.setObject(1, category.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private CategoryEntity fromResultSet(ResultSet rs) throws SQLException {
        return CategoryEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .name(rs.getString("name"))
                .archived(rs.getBoolean("archived"))
                .build();
    }

}
