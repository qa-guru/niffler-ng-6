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

import java.sql.Connection;


public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                }
        );
    }

    public void deleteSpend(SpendJson spend) {
        jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    spendDao.deleteSpend(spendEntity);
                    return null;  // Возвращаем null, так как метод ничего не возвращает
                }
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(
                            categoryDao.create(categoryEntity)
                    );
                }
        );
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    categoryDao.deleteCategory(categoryEntity);
                    return null;  // Возвращаем null, так как метод ничего не возвращает
                }
        );
    }

}