package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;

public class SpendRowMapper implements RowMapper<SpendEntity> {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
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
                        new CategoryDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                                .findById(rs.getObject("category_id", UUID.class))
                                .orElse(null)
                )
                .build();

    }

}
