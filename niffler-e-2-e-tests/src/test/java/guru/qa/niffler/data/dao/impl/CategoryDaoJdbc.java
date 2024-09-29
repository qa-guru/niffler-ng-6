package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {
    private static final Config CFG = Config.getInstance();
    private final Connection connection;

    public CategoryDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"category\" (username, name, archived) " +
                        "VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            category.setId(generatedKey);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"category\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(rs.getObject("id", UUID.class));
                    ce.setUsername(rs.getString("username"));
                    ce.setName(rs.getString("name"));
                    ce.setArchived(rs.getBoolean("archived"));
                    return Optional.of(ce);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"category\" WHERE username = ? AND name = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, categoryName);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    CategoryEntity ce = new CategoryEntity();

                    ce.setId(resultSet.getObject("id", UUID.class));
                    ce.setName(resultSet.getString("name"));
                    ce.setUsername(resultSet.getString("username"));
                    ce.setArchived(resultSet.getBoolean("archived"));

                    return Optional.of(ce);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    @Override
    public List<CategoryEntity> findAll() {
        List<CategoryEntity> categories = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"category\"");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CategoryEntity category = new CategoryEntity();
                category.setId(rs.getObject("id", UUID.class));
                category.setUsername(rs.getString("username"));
                category.setName(rs.getString("name"));
                category.setArchived(rs.getBoolean("archived"));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        List<CategoryEntity> categories = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"category\" WHERE username = ?"
        )) {
            ps.setObject(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CategoryEntity category = new CategoryEntity();

                    category.setId(rs.getObject("id", UUID.class));
                    category.setName(rs.getString("name"));
                    category.setUsername(rs.getString("username"));
                    category.setArchived(rs.getBoolean("archived"));

                    categories.add(category);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public void deleteCategory(CategoryEntity category) {

        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM category WHERE id = ?"
        )) {

            ps.setObject(1, category.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
