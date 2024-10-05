package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.SpendJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {
    private final Config CFG = Config.getInstance();

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    private TransactionTemplate transactionTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.spendJdbcUrl())
            )
    );

    private JdbcTransactionTemplate jxTransactionTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userDataJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        var category = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(category);
                    }

                    SpendEntity entity = spendDao.create(spendEntity);
                    return SpendJson.fromEntity(entity);
                }
        );
    }

    public void deleteSpend(SpendEntity spend) {
        xaTransactionTemplate.execute(() -> {
                    spendDao.deleteSpend(spend);
                    return null;
                }
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        return xaTransactionTemplate.execute(() -> {
                    List<SpendEntity> allByUsername = spendDao.findAllByUsername(username);
                    return allByUsername.stream()
                            .map(SpendJson::fromEntity)
                            .toList();
                }
        );
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
                    Optional<SpendEntity> spendById = spendDao.findById(id);
                    return spendById.map(SpendJson::fromEntity);
                }
        );
    }
}
