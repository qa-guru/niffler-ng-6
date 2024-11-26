package guru.qa.niffler.data.dao.mapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.enums.AuthorityEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityEntityRowMapper implements RowMapper<AuthAuthorityEntity> {

  public static final AuthAuthorityEntityRowMapper instance = new AuthAuthorityEntityRowMapper();

  public AuthAuthorityEntityRowMapper() {
  }

  @Override
  public AuthAuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthAuthorityEntity ue = new AuthAuthorityEntity();
    ue.setId(rs.getObject("id", UUID.class));
    ue.setUserId(rs.getObject("user_id", UUID.class));
    ue.setAuthority(AuthorityEnum.valueOf(rs.getString("authority")));
    return ue;
  }
}
