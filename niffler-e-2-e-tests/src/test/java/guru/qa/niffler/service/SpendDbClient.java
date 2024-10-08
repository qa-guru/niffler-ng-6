package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.swing.text.html.Option;

import static guru.qa.niffler.data.Databases.transaction;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();


    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson create(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(spendDao.create(spendEntity));
                }
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
                }
                );
    }

    public CategoryJson updateCategoryArchivedStatus(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(categoryDao.updateArchived(categoryEntity));
                }
                );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return jdbcTxTemplate.execute(() -> {
                    Optional<CategoryEntity> ce = categoryDao.findCategoryByUsernameAndCategoryName(username, name);
                    return ce.map(CategoryJson::fromEntity);
                }
                );
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    categoryDao.deleteCategory(categoryEntity);
                    return null;
                }
                );
    }
}