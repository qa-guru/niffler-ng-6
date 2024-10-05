package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class CategoryDbClient {
    private final Config CFG = Config.getInstance();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    private final JdbcTransactionTemplate jxTransactionTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userDataJdbcUrl()
    );

    public CategoryJson createCategory(CategoryEntity category) {
        return xaTransactionTemplate.execute(() -> {
                    CategoryEntity categoryEntity = categoryDao.create(category);
                    return CategoryJson.fromEntity(categoryEntity);
                }
        );
    }

    public void deleteCategory(CategoryEntity category) {
        xaTransactionTemplate.execute(() -> {
                    categoryDao.delete(category);
                    return null;
                }
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return xaTransactionTemplate.execute(() -> {
                    Optional<CategoryEntity> categoryEntity =
                            categoryDao
                                    .findCategoryByUsernameAndCategoryName(username, categoryName);
                    return categoryEntity.map(CategoryJson::fromEntity);
                }
        );
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
                    Optional<CategoryEntity> categoryById = categoryDao.findById(id);

                    return categoryById.map(CategoryJson::fromEntity);
                }
        );
    }

    public List<CategoryJson> findAllByUsername(String username) {
        return xaTransactionTemplate.execute(() -> {
                    List<CategoryEntity> allByUsername = categoryDao.findAllByUsername(username);
                    return allByUsername.stream()
                            .map(CategoryJson::fromEntity)
                            .toList();
                }
        );
    }
}
