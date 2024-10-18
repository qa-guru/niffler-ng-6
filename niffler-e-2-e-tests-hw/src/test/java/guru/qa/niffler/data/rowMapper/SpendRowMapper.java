package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class SpendRowMapper implements RowMapper<SpendEntity> {

    public static final SpendRowMapper INSTANCE = new SpendRowMapper();

    private SpendRowMapper() {
    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return SpendEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .spendDate(new Date(rs.getDate("spend_date").getTime()))
                .currency(CurrencyValues.valueOf(rs.getString("currency")))
                .amount(rs.getDouble("amount"))
                .description(rs.getString("description"))
                .category(
                        new CategoryDaoSpringJdbc()
                                .findById(rs.getObject("category_id", UUID.class))
                                .orElse(null)
                )
                .build();

    }

}
