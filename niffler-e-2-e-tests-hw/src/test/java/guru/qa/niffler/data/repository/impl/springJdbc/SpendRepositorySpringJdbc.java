package guru.qa.niffler.data.repository.impl.springJdbc;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private final SpendDao spendDao = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();

    public SpendEntity create(SpendEntity spend) {
        categoryDao.findByUsernameAndName(spend.getCategory().getUsername(), spend.getCategory().getName())
                .ifPresentOrElse(
                        spend::setCategory,
                        () -> spend.setCategory(categoryDao.create(spend.getCategory()))
                );
        return spendDao.create(spend);
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return spendDao.findById(id)
                .map(spend ->
                        spend.setCategory(categoryDao
                                .findById(spend.getCategory().getId())
                                .get()));
    }

    @Override
    public Optional<SpendEntity> findFirstSpendByUsernameAndDescription(String username, String description) {
        var spends = spendDao.findByUsernameAndDescription(username, description);
        return spends.isEmpty()
                ? Optional.empty()
                : Optional.of(spends.getFirst()
                .setCategory(categoryDao
                        .findById(spends.getFirst().getCategory().getId())
                        .get()));
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return spendDao.findAllByUsername(username).stream()
                .map(spend -> spend.setCategory(categoryDao.findById(spend.getCategory().getId()).get()))
                .toList();
    }

    @Override
    public List<SpendEntity> findByUsernameAndDescription(String username, String description) {
        return spendDao.findByUsernameAndDescription(username, description).stream()
                .map(spend -> spend.setCategory(categoryDao.findById(spend.getCategory().getId()).get()))
                .toList();
    }

    @Override
    public List<SpendEntity> findAll() {
        return spendDao.findAll().stream()
                .map(spend -> spend.setCategory(categoryDao.findById(spend.getCategory().getId()).get()))
                .toList();
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
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
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndName(String username, String name) {
        return categoryDao.findByUsernameAndName(username, name);
    }

    @Override
    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return categoryDao.findAllByUsername(username);
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        return categoryDao.update(category);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.remove(category);
    }

}