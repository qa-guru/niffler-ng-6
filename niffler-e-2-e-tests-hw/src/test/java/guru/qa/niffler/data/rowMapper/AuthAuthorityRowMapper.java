package guru.qa.niffler.data.rowMapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthAuthorityRowMapper implements RowMapper<AuthAuthorityEntity> {

    public static final AuthAuthorityRowMapper INSTANCE = new AuthAuthorityRowMapper();

    private AuthAuthorityRowMapper() {
    }

    @Override
    public @Nonnull AuthAuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return AuthAuthorityEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .user(AuthUserEntity.builder().id(rs.getObject("user_id", UUID.class)).build())
                .authority(Authority.valueOf(rs.getString("authority")))
                .build();
    }

}
