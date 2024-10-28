package guru.qa.niffler.service.db.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.jdbc.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.db.SpendDbClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class SpendDbClientJdbc implements SpendDbClient {

    private static final SpendMapper spendMapper = new SpendMapper();
    private static final CategoryMapper categoryMapper = new CategoryMapper();
    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(SPEND_JDBC_URL);
    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    @Override
    public SpendJson create(@NonNull SpendJson spendJson) {
        log.info("Creating new spend by DTO: {}", spendJson);
        return jdbcTxTemplate.execute(() ->
                spendMapper.toDto(
                        spendRepository.create(
                                spendMapper.toEntity(spendJson))));
    }

    @Override
    public Optional<SpendJson> findById(@NonNull UUID id) {
        log.info("Find spend by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                spendRepository.findById(id)
                        .map(spendMapper::toDto));
    }

    @Override
    public Optional<SpendJson> findFirstSpendByUsernameAndDescription(@NonNull String username, @NonNull String description) {
        log.info("Find first spend by username = [{}] and description = [{}]", username, description);
        return jdbcTxTemplate.execute(() ->
                spendRepository.findFirstSpendByUsernameAndDescription(username, description)
                        .map(spendMapper::toDto));
    }

    @Override
    public List<SpendJson> findAllByUsernameAndDescription(@NonNull String username, @NonNull String description) {
        log.info("Find all spends by username = [{}] and description = [{}]", username, description);
        return jdbcTxTemplate.execute(() ->
                spendRepository.findByUsernameAndDescription(username, description).stream()
                        .map(spendMapper::toDto)
                        .toList());
    }

    @Override
    public List<SpendJson> findAllByUsername(@NonNull String username) {
        log.info("Find all spends by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                spendRepository.findAllByUsername(username).stream()
                        .map(spendMapper::toDto)
                        .toList());
    }

    @Override
    public List<SpendJson> findAll() {
        log.info("Find all spends");
        return jdbcTxTemplate.execute(() ->
                spendRepository.findAll().stream()
                        .map(spendMapper::toDto)
                        .toList());
    }

    @Override
    public SpendJson update(@NonNull SpendJson spendJson) {
        log.info("Update spend: {}", spendJson);
        return jdbcTxTemplate.execute(() ->
                spendMapper.toDto(
                        spendRepository.update(
                                spendMapper.toEntity(spendJson))));
    }

    @Override
    public void remove(@NonNull SpendJson spendJson) {
        log.info("Remove spend: {}", spendJson);
        jdbcTxTemplate.execute(() -> {
            spendRepository.remove(spendMapper.toEntity(spendJson));
            return null;
        });
    }

    @Override
    public CategoryJson createCategory(@NonNull CategoryJson categoryJson) {
        log.info("Creating new category by DTO: {}", categoryJson);
        return jdbcTxTemplate.execute(() ->
                categoryMapper.toDto(
                        spendRepository.createCategory(
                                categoryMapper.toEntity(categoryJson))));
    }

    @Override
    public Optional<CategoryJson> findCategoryById(@NonNull UUID id) {
        log.info("Find category by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                spendRepository.findCategoryById(id)
                        .map(categoryMapper::toDto));
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndName(@NonNull String username, @NonNull String name) {
        log.info("Find category by username = [{}] and name = [{}]", username, name);
        return jdbcTxTemplate.execute(() ->
                spendRepository.findCategoryByUsernameAndName(username, name)
                        .map(categoryMapper::toDto));
    }

    @Override
    public List<CategoryJson> findAllCategoriesByUsername(@NonNull String username) {
        log.info("Find all categories by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                spendRepository.findAllCategoriesByUsername(username).stream()
                        .map(categoryMapper::toDto)
                        .toList());
    }

    @Override
    public List<CategoryJson> findAllCategories() {
        log.info("Find all categories");
        return jdbcTxTemplate.execute(() ->
                spendRepository.findAllCategories().stream()
                        .map(categoryMapper::toDto)
                        .toList());
    }

    @Override
    public CategoryJson updateCategory(@NonNull CategoryJson categoryJson) {
        log.info("Update category: {}", categoryJson);
        return jdbcTxTemplate.execute(() ->
                categoryMapper.toDto(
                        spendRepository.updateCategory(
                                categoryMapper.toEntity(categoryJson))));
    }

    @Override
    public void removeCategory(@NonNull CategoryJson categoryJson) {
        log.info("Remove category: {}", categoryJson);
        jdbcTxTemplate.execute(() -> {
            spendRepository.removeCategory(categoryMapper.toEntity(categoryJson));
            return null;
        });
    }

}
