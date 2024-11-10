package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserRowMapper implements RowMapper<AuthUserEntity> {

    public static final AuthUserRowMapper INSTANCE = new AuthUserRowMapper();

    private AuthUserRowMapper() {
    }

    @Override
    public @Nonnull AuthUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return AuthUserEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .enabled(rs.getBoolean("enabled"))
                .accountNonExpired(rs.getBoolean("account_non_expired"))
                .accountNonLocked(rs.getBoolean("account_non_locked"))
                .credentialsNonExpired(rs.getBoolean("credentials_non_expired"))
                .build();
    }

}
