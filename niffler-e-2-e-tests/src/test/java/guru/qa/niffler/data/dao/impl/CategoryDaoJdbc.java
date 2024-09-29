package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {
    private final Config CFG = Config.getInstance();

    @Override
    public CategoryJson createCategory(CategoryEntity category) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
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
                        throw new SQLException("Can't find id from ResultSet");
                    }
                }
                category.setId(generatedKey);
                return CategoryJson.fromEntity(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"category\" WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        CategoryEntity category = new CategoryEntity();
                        category.setId(rs.getObject("id", UUID.class));
                        category.setName(rs.getString("name"));
                        category.setArchived(rs.getBoolean("archived"));
                        category.setUsername(rs.getString("username"));
                        CategoryJson categoryJson = CategoryJson.fromEntity(category);
                        return Optional.of(categoryJson);
                    } else {
                        return Optional.empty();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"category\" WHERE username=? AND name=?"
            )) {
                ps.setString(1, username);
                ps.setString(2, categoryName);

                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        CategoryEntity category = getCategoryEntity(rs);
                        CategoryJson categoryJson = CategoryJson.fromEntity(category);
                        return Optional.of(categoryJson);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategoryJson> findAllByUsername(String username) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM \"category\" WHERE username=?"
            )) {

                preparedStatement.setString(1, username);

                preparedStatement.execute();
                List<CategoryJson> categoryList = new ArrayList<>();
                try (ResultSet rs = preparedStatement.getResultSet()) {
                    while (rs.next()) {
                        CategoryEntity category = getCategoryEntity(rs);
                        categoryList.add(CategoryJson.fromEntity(category));
                    }
                    return categoryList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CategoryEntity getCategoryEntity(ResultSet rs) throws SQLException {
        CategoryEntity category = new CategoryEntity();
        category.setName(rs.getString("name"));
        category.setUsername(rs.getString("username"));
        category.setArchived(rs.getBoolean("archived"));
        category.setId(rs.getObject("id", UUID.class));
        return category;
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM \"category\" WHERE id=?"
            )) {
                ps.setObject(1, category.getId());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
