package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

@Slf4j
public class SpendDbClient {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final SpendMapper spendMapper = new SpendMapper();

    public SpendJson create(SpendJson spendJson) {

        log.info("Creating new spend by DTO: {}", spendJson);
        return transaction(connection -> {
                    SpendEntity spendEntity = spendMapper.toEntity(spendJson);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory().setUsername(spendJson.getUsername()));
                        spendEntity.setCategory(categoryEntity);
                    }
                    return spendMapper.toDto(new SpendDaoJdbc(connection).create(spendEntity));
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<SpendJson> findById(UUID id) {
        log.info("Get spend by id = [{}]", id);
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection)
                            .findById(id)
                            .map(spendMapper::toDto);
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<SpendJson> findByUsernameAndDescription(String username, String description) {
        log.info("Get spend by username = [{}] and description = [{}]", username, description);
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection)
                            .findByUsernameAndDescription(username, description)
                            .map(spendMapper::toDto);
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        log.info("Get all spends by username = [{}]", username);
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection)
                            .findAllByUsername(username).stream()
                            .map(spendMapper::toDto)
                            .toList();
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void delete(UUID id) {
        log.info("Remove spend by id = [{}]", id);
        transaction(connection -> {
                    SpendDao spendDao = new SpendDaoJdbc(connection);
                    new SpendDaoJdbc(connection).findById(id)
                            .ifPresent(spendDao::delete);
                    return null;
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

}
