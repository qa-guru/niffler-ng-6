package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private final SpendRepository spendRepository = new SpendRepositoryHibernate();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            // Проверка на существование категории по ID
            if (spendEntity.getCategory().getId() != null) {
                // Если категория уже существует, обновляем её
                spendEntity.setCategory(spendRepository.updateCategory(spendEntity.getCategory()));
            } else {
                CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(spendRepository.create(spendEntity));
        });
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            SpendEntity updatedEntity = spendRepository.update(spendEntity);
            return SpendJson.fromEntity(updatedEntity);
        });
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepository.findById(id);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    @Override
    public List<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            List<SpendEntity> spendEntities = spendRepository.findByUsernameAndSpendDescription(username, description);
            // Преобразуем список SpendEntity в список SpendJson
            return spendEntities.stream()
                    .map(SpendJson::fromEntity)
                    .toList();
        });
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepository.remove(spendEntity);
            return null; // Возвращаем null, так как метод ничего не возвращает
        });
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            CategoryEntity createdCategoryEntity = spendRepository.createCategory(categoryEntity);
            return CategoryJson.fromEntity(createdCategoryEntity);
        });
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            CategoryEntity updatedEntity = spendRepository.updateCategory(categoryEntity);
            return CategoryJson.fromEntity(updatedEntity);
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryById(id);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryByUsernameAndCategoryName(username, name);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepository.removeCategory(categoryEntity);
            return null; // Возвращаем null, так как метод ничего не возвращает
        });
    }
}