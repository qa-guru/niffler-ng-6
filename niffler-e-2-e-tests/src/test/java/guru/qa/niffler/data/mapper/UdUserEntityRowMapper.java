package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UdUserEntityRowMapper implements RowMapper<UserEntity> {

    public static final UdUserEntityRowMapper instance = new UdUserEntityRowMapper();

    private UdUserEntityRowMapper() {

    }

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setFirstname(rs.getString("firstname"));
        user.setFullname(rs.getString("full_name"));
        user.setUsername(rs.getString("username"));
        user.setSurname(rs.getString("surname"));
        user.setPhoto(rs.getBytes("photo"));
        user.setPhotoSmall(rs.getBytes("photo_small"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        return user;
    }
}
