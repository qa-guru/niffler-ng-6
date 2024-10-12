package guru.qa.niffler.test.web.database;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class JdbcTest {

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    void daoTest(@CreateNewUser UserModel user){

        SpendJson spend = SpendUtils.generate();
        spend.setUsername(user.getUsername())
                .getCategory().setUsername(user.getUsername());
        spend = spendDbClient.createSpend(spend);

        log.info("Created spend: {}", spend);
        Assertions.assertNotNull(spend.getId());

    }

}
