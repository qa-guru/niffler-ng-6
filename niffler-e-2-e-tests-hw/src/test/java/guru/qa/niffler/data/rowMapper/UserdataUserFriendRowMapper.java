package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.rest.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserFriendRowMapper implements RowMapper<UserEntity> {

    public static final UserdataUserFriendRowMapper INSTANCE = new UserdataUserFriendRowMapper();

    private UserdataUserFriendRowMapper() {
    }

    @Override
    public @Nonnull UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .currency(CurrencyValues.valueOf(rs.getString("currency")))
                .firstName(rs.getString("firstname"))
                .surname(rs.getString("surname"))
                .fullName(rs.getString("full_name"))
                .build();
    }

}
