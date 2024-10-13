package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserEntityRowMapper implements RowMapper<AuthUserEntity> {

    public static final AuthUserEntityRowMapper instance = new AuthUserEntityRowMapper();

    private AuthUserEntityRowMapper() {
    }

    @Override
    public AuthUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUserEntity result = new AuthUserEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setPassword(rs.getString("password"));
        result.setEnabled(rs.getBoolean("enabled"));
        result.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        result.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        result.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return result;
    }
}