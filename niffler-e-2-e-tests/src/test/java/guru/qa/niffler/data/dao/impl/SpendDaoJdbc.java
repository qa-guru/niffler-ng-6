package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public SpendDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, spend.getUsername());
                ps.setDate(2, spend.getSpendDate());
                ps.setString(3, spend.getCurrency().name());
                ps.setDouble(4, spend.getAmount());
                ps.setString(5, spend.getDescription());
                ps.setObject(6, spend.getCategory().getId());

                ps.executeUpdate();
                final UUID generationKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generationKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in ResultSet");
                    }
                }
                spend.setId(generationKey);
                return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        SpendEntity se = new SpendEntity();
                        se.setId(rs.getObject("id", UUID.class));
                        se.setUsername(rs.getString("username"));
                        se.setSpendDate(rs.getDate("spend_date"));
                        se.setCurrency(rs.getObject("currency", CurrencyValues.class));
                        se.setAmount(rs.getDouble("amount"));
                        se.setDescription(rs.getString("description"));
                        se.setCategory(rs.getObject("category_id", CategoryEntity.class));
                        return Optional.of(se);
                    } else {
                        return Optional.empty();
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE username = ?"
            )) {
                ps.setString(1, username);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    List<SpendEntity> list = new ArrayList<>();
                    while (rs.next()) {
                        SpendEntity se = new SpendEntity();
                        se.setId(rs.getObject("id", UUID.class));
                        se.setUsername(rs.getString("username"));
                        se.setSpendDate(rs.getDate("spend_date"));
                        se.setCurrency(rs.getObject("currency", CurrencyValues.class));
                        se.setAmount(rs.getDouble("amount"));
                        se.setDescription(rs.getString("description"));
                        se.setCategory(rs.getObject("category_id", CategoryEntity.class));
                        list.add(se);
                    }
                    return list; // return List.of();
                }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteSpend(SpendEntity spend) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM spend WHERE id = ?"
            )) {
                ps.setObject(1, spend.getId());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                    } else {
                        throw new SQLException("Can't find deleted spend");
                    }
                }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
