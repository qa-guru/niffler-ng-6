package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class CategoryRowMapper implements RowMapper<CategoryEntity> {

    public static final CategoryRowMapper INSTANCE = new CategoryRowMapper();

    private CategoryRowMapper() {
    }

    @Override
    public @Nonnull CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return CategoryEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .name(rs.getString("name"))
                .username(rs.getString("username"))
                .archived(rs.getBoolean("archived"))
                .build();
    }

}
