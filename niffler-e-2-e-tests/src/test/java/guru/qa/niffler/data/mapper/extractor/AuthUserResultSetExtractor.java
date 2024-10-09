package guru.qa.niffler.data.mapper.extractor;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserResultSetExtractor implements ResultSetExtractor<Map<UUID, AuthUserEntity>> {

    @Override
    public Map<UUID, AuthUserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
        while (rs.next()) {
            UUID userId = rs.getObject("id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(userId, id -> {
                AuthUserEntity newUser = new AuthUserEntity();
                try {
                    newUser.setId(id);
                    newUser.setUsername(rs.getString("username"));
                    newUser.setPassword(rs.getString("password"));
                    newUser.setEnabled(rs.getBoolean("enabled"));
                    newUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    newUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    newUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    newUser.setAuthorities(new ArrayList<>());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return newUser;
            });

            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("authority_id", UUID.class));
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            authority.setUser(user);
            user.getAuthorities().add(authority);
        }
        return userMap;
    }
}