package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendEntity create(SpendEntity spend) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, spend.getUsername());
                ps.setDate(2, spend.getSpendDate());
                ps.setString(3, spend.getCurrency().name());
                ps.setDouble(4, spend.getAmount());
                ps.setString(5, spend.getDescription());
                ps.setObject(6, spend.getCategory().getId());

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
    public Optional<SpendEntity> findById(UUID id) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE id = ?",
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
    public Optional<SpendEntity> findByUsernameAndDescription(String username, String description) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE username = ? AND description = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, username);
                ps.setString(2, description);
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
    public List<SpendEntity> findAllByUsername(String username) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE username = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {

                ps.setString(1, username);
                ps.executeQuery();

                try (ResultSet rs = ps.getResultSet()) {
                    List<SpendEntity> spends = new ArrayList<>();
                    while (rs.next())
                        spends.add(fromResultSet(rs));
                    return spends;
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(SpendEntity spend) {

        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM spend WHERE id = ?"
            )) {
                ps.setObject(1, spend.getId());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private SpendEntity fromResultSet(ResultSet rs) throws SQLException {
        return SpendEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .spendDate(rs.getDate("spend_date"))
                .currency(CurrencyValues.valueOf(rs.getString("currency")))
                .amount(rs.getDouble("amount"))
                .description(rs.getString("description"))
                .category(categoryDao.findById(rs.getObject("category_id", UUID.class)).orElse(null))
                .build();
    }

}
