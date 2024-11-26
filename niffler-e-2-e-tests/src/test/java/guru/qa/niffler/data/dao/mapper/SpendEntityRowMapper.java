package guru.qa.niffler.data.dao.mapper;

import guru.qa.niffler.data.entity.category.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.enums.CurrencyValuesEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

  public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

  public SpendEntityRowMapper() {
  }

  @Override
  public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    SpendEntity se = new SpendEntity();
    se.setId(rs.getObject("id", UUID.class));
    se.setUsername(rs.getString("username"));
    se.setSpendDate(rs.getDate("spend_date"));
    se.setCurrency(CurrencyValuesEnum.valueOf(rs.getString("currency")));
    se.setAmount(rs.getDouble("amount"));
    se.setDescription(rs.getString("description"));

    CategoryEntity category = new CategoryEntity();
    category.setId(rs.getObject("category_id", UUID.class));
    se.setCategory(category);
    return se;
  }
}
