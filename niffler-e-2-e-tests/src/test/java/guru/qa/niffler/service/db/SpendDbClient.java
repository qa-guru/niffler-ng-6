package guru.qa.niffler.service.db;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import javax.annotation.ParametersAreNonnullByDefault;

import static guru.qa.niffler.data.Databases.transaction;

import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    public SpendJson create(SpendJson spend) {
        return xaTxTemplate.execute(() -> SpendJson.fromEntity(
                        spendRepository.create(SpendEntity.fromJson(spend))
                )
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTxTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        );
    }

    @Override
    public CategoryJson updateCategoryArchivedStatus(CategoryJson category) {
        return xaTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    Optional<SpendEntity> spendEntity = spendRepository.findById(categoryEntity.getId());
                    spendEntity.ifPresent(spendEntity1 -> spendEntity1.setCategory(categoryEntity));
                    return CategoryJson.fromEntity(categoryEntity);
                }
        );
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return xaTxTemplate.execute(() -> {
                    Optional<CategoryEntity> ce = spendRepository.findCategoryByUsernameAndCategoryName(username, name);
                    return ce.map(CategoryJson::fromEntity);
                }
        );
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        xaTxTemplate.execute(() -> {
            spendRepository.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}