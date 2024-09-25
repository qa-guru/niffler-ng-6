package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {
        String sql = "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setSpendParameters(ps, spend);
            ps.executeUpdate();

            spend.setId(getGeneratedKey(ps));
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        String sql = "SELECT * FROM spend WHERE id = ?";
        return Optional.ofNullable(executeQuery(sql, ps -> ps.setObject(1, id)));
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        String sql = "SELECT * FROM spend WHERE username = ?";
        return executeQueryList(sql, ps -> ps.setString(1, username));
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        String sql = "DELETE FROM spend WHERE id = ?";
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, spend.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpendEntity mapToSpendEntity(ResultSet rs) throws SQLException {
        SpendEntity spend = new SpendEntity();
        spend.setId(rs.getObject("id", UUID.class));
        spend.setUsername(rs.getString("username"));
        spend.setSpendDate(rs.getDate("spend_date"));
        spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spend.setAmount(rs.getDouble("amount"));
        spend.setDescription(rs.getString("description"));

        UUID categoryId = rs.getObject("category_id", UUID.class);
        CategoryEntity category = categoryDao.findCategoryById(categoryId)
                .orElseThrow(() -> new SQLException("Category not found for id: " + categoryId));
        spend.setCategory(category);

        return spend;
    }

    private SpendEntity executeQuery(String sql, PreparedStatementSetter setter) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(sql)) {

            setter.setValues(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToSpendEntity(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SpendEntity> executeQueryList(String sql, PreparedStatementSetter setter) {
        List<SpendEntity> spends = new ArrayList<>();
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(sql)) {

            setter.setValues(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    spends.add(mapToSpendEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spends;
    }

    private UUID getGeneratedKey(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getObject("id", UUID.class);
            } else {
                throw new SQLException("Can't find id in ResultSet");
            }
        }
    }

    private void setSpendParameters(PreparedStatement ps, SpendEntity spend) throws SQLException {
        ps.setString(1, spend.getUsername());
        ps.setDate(2, spend.getSpendDate());
        ps.setString(3, spend.getCurrency().name());
        ps.setDouble(4, spend.getAmount());
        ps.setString(5, spend.getDescription());
        ps.setObject(6, spend.getCategory().getId());
    }

    @FunctionalInterface
    interface PreparedStatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }
}
