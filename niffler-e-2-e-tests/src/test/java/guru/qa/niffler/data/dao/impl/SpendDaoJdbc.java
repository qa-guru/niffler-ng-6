package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;

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

    @Override
    public SpendJson createSpend(SpendEntity spend) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) " +
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

                final UUID generatedKey;

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in ResultSet");
                    }
                }

                spend.setId(generatedKey);
                return SpendJson.fromEntity(spend);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"spend\" WHERE id=?"
            )) {
                ps.setObject(1, id);

                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        SpendEntity spend = getSpendEntityFromResultSet(rs);
                        SpendJson spendJson = SpendJson.fromEntity(spend);
                        return Optional.of(spendJson);
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
    public List<SpendJson> findAllByUsername(String username) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM \"spend\" WHERE username=?"
            )) {

                preparedStatement.setString(1, username);

                preparedStatement.execute();
                List<SpendJson> spendJsonList = new ArrayList<>();
                try (ResultSet rs = preparedStatement.getResultSet()) {
                    while (rs.next()) {
                        SpendEntity spend = getSpendEntityFromResultSet(rs);
                        spendJsonList.add(SpendJson.fromEntity(spend));
                    }
                    return spendJsonList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        try (Connection connection = Databases.getConnection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM \"spend\" WHERE id=?"
            )) {
                ps.setObject(1, spend.getId());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpendEntity getSpendEntityFromResultSet(ResultSet rs) throws SQLException {
        SpendEntity spend = new SpendEntity();
        spend.setSpendDate(rs.getDate("spend_date"));
        spend.setDescription(rs.getString("description"));
        spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spend.setAmount(rs.getDouble("amount"));
        spend.setUsername(rs.getString("username"));
        spend.setId(rs.getObject("id", UUID.class));

        Optional<CategoryJson> categoryEntity =
                categoryDao.findCategoryById(rs.getObject("category_id", UUID.class));

        categoryEntity.ifPresent(categoryJson -> spend.setCategory(CategoryEntity.fromJson(categoryJson)));

        return spend;
    }
}
