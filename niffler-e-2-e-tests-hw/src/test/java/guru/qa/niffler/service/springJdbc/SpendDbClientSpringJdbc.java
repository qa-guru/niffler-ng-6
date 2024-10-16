package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

@Slf4j
public class SpendDbClientSpringJdbc {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final SpendMapper spendMapper = new SpendMapper();

    public SpendJson create(SpendJson spendJson) {

        log.info("Creating new spend by DTO: {}", spendJson);
        return null;
    }

    public Optional<SpendJson> findById(UUID id) {
        log.info("Get spend by id = [{}]", id);
        return Optional.empty();
    }

    public Optional<SpendJson> findByUsernameAndDescription(String username, String description) {
        log.info("Get spend by username = [{}] and description = [{}]", username, description);
        return Optional.empty();
    }

    public List<SpendJson> findAllByUsername(String username) {
        log.info("Get all spends by username = [{}]", username);
        return null;
    }

    public void delete(UUID id) {
        log.info("Remove spend by id = [{}]", id);
    }

}
