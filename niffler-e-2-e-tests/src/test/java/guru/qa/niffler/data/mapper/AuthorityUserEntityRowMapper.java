package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorityUserEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthorityUserEntityRowMapper instance = new AuthorityUserEntityRowMapper();

    private AuthorityUserEntityRowMapper() {

    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setId(rs.getObject("id", UUID.class));
        authority.setAuthority(rs.getObject("authority", Authority.class));
        authority.setUser(rs.getObject("user_id", AuthUserEntity.class));
        return authority;
    }
}
