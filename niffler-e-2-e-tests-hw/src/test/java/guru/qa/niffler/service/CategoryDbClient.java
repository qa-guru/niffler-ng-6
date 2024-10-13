package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CategoryDbClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final CategoryMapper categoryMapper = new CategoryMapper();

    public CategoryJson create(CategoryJson categoryJson) {
        log.info("Creating new category by DTO: {}", categoryJson);
        return categoryMapper.toDto(categoryDao.create(categoryMapper.toEntity(categoryJson)));
    }

    public Optional<CategoryJson> findById(UUID id) {
        log.info("Get category by id = [{}]", id);
        return categoryDao.findById(id).map(categoryMapper::toDto);
    }

    public Optional<CategoryJson> findByUsernameAndName(String username, String name) {
        log.info("Get category by username = [{}] and name = [{}]", username, name);
        return categoryDao.findByUsernameAndName(username, name).map(categoryMapper::toDto);
    }

    public List<CategoryJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return categoryDao.findAllByUsername(username).stream().map(categoryMapper::toDto).toList();
    }

    public void delete(UUID id) {
        log.info("Remove category by id = [{}]", id);
        Optional.ofNullable(categoryDao.findById(id))
                .ifPresent(category -> categoryDao.delete(category.orElse(null)));
    }

}
