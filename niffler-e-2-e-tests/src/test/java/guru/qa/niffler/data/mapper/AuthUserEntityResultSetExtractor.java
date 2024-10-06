package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserEntityResultSetExtractor implements ResultSetExtractor<AuthUserEntity> {

    public static final AuthUserEntityResultSetExtractor instance = new AuthUserEntityResultSetExtractor();

    public AuthUserEntityResultSetExtractor() {
    }

    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> authUserEntityMap = new ConcurrentHashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            AuthUserEntity user = authUserEntityMap.computeIfAbsent(
                    userId,
                    key -> {
                        AuthUserEntity authUserEntity = new AuthUserEntity();
                        try {
                            authUserEntity.setId(rs.getObject("id", UUID.class));
                            authUserEntity.setUsername(rs.getString("username"));
                            authUserEntity.setPassword(rs.getString("password"));
                            authUserEntity.setEnabled(rs.getBoolean("enabled"));
                            authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                            authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                            authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                            return authUserEntity;
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("authority_id", UUID.class));
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            user.getAuthorities().add(authority);
        }
        return authUserEntityMap.get(userId);
    }
}
