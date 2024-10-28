package guru.qa.niffler.data.dao.imp.spring;

import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserDaoSpringJdbc implements UdUserDao {

  private final DataSource dataSource;

  public UdUserDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public UdUserEntity create(UdUserEntity udUser) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
              "VALUES(?, ?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, udUser.getUsername());
      ps.setString(2, udUser.getCurrency().name());
      ps.setString(3, udUser.getFirstname());
      ps.setString(4, udUser.getSurname());
      ps.setBytes(5, udUser.getPhoto());
      ps.setBytes(6, udUser.getPhotoSmall());
      ps.setString(7, udUser.getFullname());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    udUser.setId(generatedKey);
    return udUser;
  }

  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    return Optional.ofNullable(
        new JdbcTemplate(dataSource).queryForObject(
            "SELECT * FROM \"user\" WHERE id = ?",
            UdUserEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<UdUserEntity> findAll() {
    return new JdbcTemplate(dataSource).query(
        "SELECT * FROM \"user\"",
        UdUserEntityRowMapper.instance
    );
  }
}
