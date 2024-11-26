package guru.qa.niffler.data.dao.mapper;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.enums.CurrencyValuesEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UdUserEntityRowMapper implements RowMapper<UdUserEntity> {

  public static final UdUserEntityRowMapper instance = new UdUserEntityRowMapper();

  public UdUserEntityRowMapper() {
  }

  @Override
  public UdUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UdUserEntity result = new UdUserEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setCurrency(CurrencyValuesEnum.valueOf(rs.getString("currency")));
    result.setFirstname(rs.getString("firstname"));
    result.setSurname(rs.getString("surname"));
    result.setPhoto(rs.getBytes("photo"));
    result.setPhotoSmall(rs.getBytes("photo_small"));
    result.setFullname(rs.getString("full_name"));
    return result;
  }
}
