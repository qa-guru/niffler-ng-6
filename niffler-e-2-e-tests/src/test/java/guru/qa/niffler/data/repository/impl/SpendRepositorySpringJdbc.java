package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

  private static final Config CFG = Config.getInstance();

  private final String url = CFG.spendJdbcUrl();
  private final SpendDao spendDao = new SpendDaoSpringJdbc();
  private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();

  @Override
  public SpendEntity create(SpendEntity spend) {
    final UUID categoryId = spend.getCategory().getId();
    if (categoryId == null && categoryDao.findById(categoryId).isEmpty()) {
      spend.setCategory(
          categoryDao.create(spend.getCategory())
      );
    }
    return spendDao.create(spend);
  }

  @Override
  public SpendEntity update(SpendEntity spend) {
    spendDao.update(spend);
    categoryDao.update(spend.getCategory());
    return spend;
  }

  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    return categoryDao.create(category);
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return categoryDao.findById(id);
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM category WHERE username = ? and name = ?",
            CategoryEntityRowMapper.instance,
            username,
            name
        )
    );
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    return spendDao.findById(id);
  }

  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM spend WHERE username = ? and description = ?",
            SpendEntityRowMapper.instance,
            username,
            description
        )
    );
  }

  @Override
  public void remove(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    jdbcTemplate.update("DELETE FROM spend WHERE id = ?", spend.getId());
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    jdbcTemplate.update("DELETE FROM category WHERE id = ?", category.getId());
  }
}
