package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendRepositoryJdbc implements SpendRepository {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public @Nonnull SpendEntity create(SpendEntity spend) {
        categoryDao.findByUsernameAndName(spend.getCategory().getUsername(), spend.getCategory().getName())
                .ifPresentOrElse(
                        spend::setCategory,
                        () -> spend.setCategory(categoryDao.create(spend.getCategory()))
                );
        return spendDao.create(spend);
    }

    @Override
    public @Nonnull Optional<SpendEntity> findById(UUID id) {
        return spendDao.findById(id)
                .map(spend ->
                        spend.setCategory(categoryDao
                                .findById(spend.getCategory().getId())
                                .get()));
    }

    @Override
    public @Nonnull Optional<SpendEntity> findFirstSpendByUsernameAndDescription(String username, String description) {
        var spends = spendDao.findByUsernameAndDescription(username, description);
        return spends.isEmpty()
                ? Optional.empty()
                : Optional.of(spends.getFirst()
                .setCategory(categoryDao
                        .findById(spends.getFirst().getCategory().getId())
                        .get()));
    }

    @Override
    public @Nonnull List<SpendEntity> findAllByUsername(String username) {
        return spendDao.findAllByUsername(username).stream()
                .map(spend -> spend.setCategory(categoryDao.findById(spend.getCategory().getId()).get()))
                .toList();
    }

    @Override
    public @Nonnull List<SpendEntity> findByUsernameAndDescription(String username, String description) {
        return spendDao.findByUsernameAndDescription(username, description).stream()
                .map(spend -> spend.setCategory(categoryDao.findById(spend.getCategory().getId()).get()))
                .toList();
    }

    @Override
    public @Nonnull List<SpendEntity> findAll() {
        return spendDao.findAll().stream()
                .map(spend -> spend.setCategory(categoryDao.findById(spend.getCategory().getId()).get()))
                .toList();
    }

    @Override
    public @Nonnull SpendEntity update(SpendEntity spend) {
        categoryDao.findByUsernameAndName(spend.getCategory().getUsername(), spend.getCategory().getName())
                .ifPresentOrElse(
                        spend::setCategory,
                        () -> spend.setCategory(categoryDao.create(spend.getCategory()))
                );
        return spendDao.update(spend);
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDao.remove(spend);
    }

    @Override
    public @Nonnull CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public @Nonnull Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findById(id);
    }

    @Override
    public @Nonnull Optional<CategoryEntity> findCategoryByUsernameAndName(String username, String name) {
        return categoryDao.findByUsernameAndName(username, name);
    }

    @Override
    public @Nonnull List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return categoryDao.findAllByUsername(username);
    }

    @Override
    public @Nonnull List<CategoryEntity> findAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public @Nonnull CategoryEntity updateCategory(CategoryEntity category) {
        return categoryDao.update(category);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.remove(category);
    }

}
