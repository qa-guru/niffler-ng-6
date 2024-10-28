package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import lombok.NonNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();

    public SpendEntity create(@NonNull SpendEntity spend) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {

            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
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


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<SpendEntity> findById(@NonNull UUID id) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"spend\" WHERE id = ?"
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
    public List<SpendEntity> findByUsernameAndDescription(@NonNull String username, @NonNull String description) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"spend\" WHERE username = ? AND description = ?"
        )) {

            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();

            List<SpendEntity> spends = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    spends.add(fromResultSet(rs));
                }
                return spends;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<SpendEntity> findAllByUsername(@NonNull String username) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"spend\" WHERE username = ?"
        )) {

            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                List<SpendEntity> spends = new ArrayList<>();
                while (rs.next())
                    spends.add(fromResultSet(rs));
                return spends;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<SpendEntity> findAll() {
        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"spend\""
        )) {

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                List<SpendEntity> spends = new ArrayList<>();
                while (rs.next())
                    spends.add(fromResultSet(rs));
                return spends;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public SpendEntity update(@NonNull SpendEntity spend) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "UPDATE \"spend\" SET username = ?, spend_date = ?, currency = ?, amount = ?, description = ?, category_id = ? WHERE id = ?"
        )) {

            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            ps.setObject(7, spend.getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                } else {
                    throw new SQLException("Could not find 'id' in ResultSet");
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(@NonNull SpendEntity spend) {

        try (PreparedStatement ps = holder(SPEND_JDBC_URL).connection().prepareStatement(
                "DELETE FROM \"spend\" WHERE id = ?"
        )) {
            ps.setObject(1, spend.getId());
            ps.executeUpdate();

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
                .category(new CategoryDaoJdbc().findById(rs.getObject("category_id", UUID.class)).orElse(null))
                .build();
    }

}
