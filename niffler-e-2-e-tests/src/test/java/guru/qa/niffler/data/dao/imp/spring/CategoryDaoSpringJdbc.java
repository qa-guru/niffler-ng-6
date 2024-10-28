package guru.qa.niffler.data.dao.imp.spring;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.entity.category.CategoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoSpringJdbc implements CategoryDao {
  private final DataSource dataSource;

  public CategoryDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public CategoryEntity create(CategoryEntity category) {
    KeyHolder kh = new GeneratedKeyHolder();

    new JdbcTemplate(dataSource).update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO category (username, name, archived) " +
              "VALUES(?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, category.getUsername());
      ps.setString(2, category.getName());
      ps.setBoolean(3, category.isArchived());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    category.setId(generatedKey);
    return category;
  }

  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    return Optional.ofNullable(
        new JdbcTemplate(dataSource).queryForObject(
            "SELECT * FROM category WHERE id = ?",
            CategoryEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<CategoryEntity> findAll() {
    return new JdbcTemplate(dataSource).query(
        "SELECT * FROM category",
        CategoryEntityRowMapper.instance);
  }

  @Override
  public void deleteById(UUID id) {
    new JdbcTemplate(dataSource).queryForObject(
        "DELETE FROM category WHERE id = ?",
        CategoryEntityRowMapper.instance,
        id
    );
  }
}
