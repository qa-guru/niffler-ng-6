package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.ex.SpendNotFoundException;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendMapper spendMapper = new SpendMapper();

    public SpendJson create(SpendJson spendJson) {

        log.info("Creating new spend by DTO: {}", spendJson);

        SpendEntity spendEntity = spendMapper.toEntity(spendJson);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory().setUsername(spendJson.getUsername()));
            spendEntity.setCategory(categoryEntity);
        }

        return spendMapper.toDto(spendDao.create(spendEntity));

    }

    public Optional<SpendJson> findById(UUID id) {
        log.info("Get spend by id = [{}]", id);
        return spendDao.findById(id).map(spendMapper::toDto);
    }

    public Optional<SpendJson> findByUsernameAndDescription(String username, String description) {
        log.info("Get spend by username = [{}] and description = [{}]", username, description);
        return spendDao.findByUsernameAndDescription(username, description).map(spendMapper::toDto);
    }

    public List<SpendJson> findAllByUsername(String username) {
        log.info("Get all spends by username = [{}]", username);
        return spendDao.findAllByUsername(username).stream().map(spendMapper::toDto).toList();
    }

    public void delete(UUID id) {
        log.info("Remove spend by id = [{}]", id);
        SpendEntity spend = spendDao.findById(id)
                .orElseThrow(() -> new SpendNotFoundException("Spend with id = [" + id + "] not found"));
        spendDao.delete(spend);
    }

}
