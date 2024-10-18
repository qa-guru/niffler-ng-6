package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class CategoryDaoJdbc implements CategoryDao {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();

    public CategoryEntity create(CategoryEntity category) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"category\" (username, name, archived) VALUES (?, ?, ?)",
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<CategoryEntity> findById(UUID id) {


        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"category\" WHERE id = ?"
        )) {

            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                return rs.next()
                        ? Optional.of(fromResultSet(rs))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<CategoryEntity> findByUsernameAndName(String username, String categoryName) {


        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"category\" WHERE username = ? AND name = ?"
        )) {

            ps.setString(1, username);
            ps.setString(2, categoryName);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                return rs.next()
                        ? Optional.of(fromResultSet(rs))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {


        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"category\" WHERE username = ?"
        )) {

            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                List<CategoryEntity> categories = new ArrayList<>();
                while (rs.next())
                    categories.add(fromResultSet(rs));
                return categories;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<CategoryEntity> findAll() {


        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"category\""
        )) {

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                List<CategoryEntity> categories = new ArrayList<>();
                while (rs.next())
                    categories.add(fromResultSet(rs));
                return categories;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(CategoryEntity category) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "DELETE FROM \"category\" WHERE id = ?"
        )) {
            ps.setObject(1, category.getId());
            ps.executeUpdate();

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
