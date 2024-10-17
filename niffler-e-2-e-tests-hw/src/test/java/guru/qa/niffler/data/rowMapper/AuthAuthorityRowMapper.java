package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityRowMapper implements RowMapper<AuthAuthorityEntity> {

    public static final AuthAuthorityRowMapper INSTANCE = new AuthAuthorityRowMapper();

    private AuthAuthorityRowMapper() {}

    @Override
    public AuthAuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return AuthAuthorityEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .userId(rs.getObject("user_id", UUID.class))
                .authority(Authority.valueOf(rs.getString("authority")))
                .build();
    }

}
