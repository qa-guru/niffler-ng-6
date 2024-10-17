package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.SpendDaoSpringJdbc;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;

@Slf4j
public class SpendDbClientSpringJdbc {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private final SpendMapper spendMapper = new SpendMapper();

    public SpendJson create(SpendJson spendJson) {
        log.info("Creating new spend by DTO: {}", spendJson);
        return spendMapper.toDto(new SpendDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .create(spendMapper.toEntity(spendJson)));
    }

    public Optional<SpendJson> findById(UUID id) {
        log.info("Get spend by id = [{}]", id);
        return new SpendDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findById(id).map(spendMapper::toDto);
    }

    public List<SpendJson> findByUsernameAndDescription(String username, String description) {
        log.info("Get spend by username = [{}] and name = [{}]", username, description);
        return new SpendDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findByUsernameAndDescription(username, description).stream()
                .map(spendMapper::toDto)
                .toList();
    }

    public List<SpendJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return new SpendDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findAllByUsername(username).stream()
                .map(spendMapper::toDto)
                .toList();
    }

    public List<SpendJson> findAll() {
        log.info("Get all categories");
        return new SpendDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findAll().stream()
                .map(spendMapper::toDto)
                .toList();
    }

    public void delete(SpendJson spendJson) {
        log.info("Remove spend: {}", spendJson);
        new SpendDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .delete(spendMapper.toEntity(spendJson));
    }

}
