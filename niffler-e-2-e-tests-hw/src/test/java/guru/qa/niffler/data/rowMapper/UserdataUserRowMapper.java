package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserdataUserRowMapper implements RowMapper<UserEntity> {

    public static final UserdataUserRowMapper INSTANCE = new UserdataUserRowMapper();

    private UserdataUserRowMapper() {
    }

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .currency(CurrencyValues.valueOf(rs.getString("currency")))
                .firstName(rs.getString("firstname"))
                .surname(rs.getString("surname"))
                .photo(rs.getBytes("photo"))
                .photoSmall(rs.getBytes("photo_small"))
                .fullName(rs.getString("full_name"))
                .build();
    }

}
