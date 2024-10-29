package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.rest.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserdataUserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserdataUserEntityRowMapper instance = new UserdataUserEntityRowMapper();

  private UserdataUserEntityRowMapper() {
  }

  @Override
  @Nonnull
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntity result = new UserEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    result.setFirstname(rs.getString("firstname"));
    result.setSurname(rs.getString("surname"));
    result.setFullname(rs.getString("full_name"));
    result.setPhoto(rs.getBytes("photo"));
    result.setPhotoSmall(rs.getBytes("photo_small"));
    return result;
  }
}
