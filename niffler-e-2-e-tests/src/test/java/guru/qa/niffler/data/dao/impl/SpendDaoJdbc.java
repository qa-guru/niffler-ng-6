package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.sql.*;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity spend) {
       try(Connection connection = Databases.connection(CFG.spendJdbcdUrl())) {
           try(PreparedStatement ps = connection.prepareStatement(
                   "INSERT INTO spend (id, username, spend_date, currency, amount, description, category_id)"+
                   "VALUES (?, ?, ?, ?, ?, ?, ?)",
                   Statement.RETURN_GENERATED_KEYS
           )) {
                ps.setObject(1, spend.getId());
                ps.setString(2, spend.getUsername());
                ps.setDate(3, spend.getSpendDate());
                ps.setString(4, spend.getCurrency());
                ps.setDouble(5, spend.getAmount());
                ps.setString(6, spend.getDescription());
                ps.setObject(7, spend.getCategory().getId());

                ps.executeUpdate();
                final UUID generationKey;
                try(ResultSet rs = ps.getResultSet()){
                   if(rs.next()){
                       generationKey = rs.getObject("id", UUID.class);
                   } else {
                       throw new SQLException("Can't find id in ResultSet");
                   }
               }
                spend.setId(generationKey);
                return spend;
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }
}
