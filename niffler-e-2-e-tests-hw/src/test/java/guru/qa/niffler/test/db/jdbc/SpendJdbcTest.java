package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.impl.jdbc.SpendDbClientJdbc;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SpendJdbcTest {

    private final SpendDbClientJdbc spendDbClient = new SpendDbClientJdbc();

    @Test
    void shouldCreateNewSpendTest(@CreateNewUser UserModel user) {
        var spend = spendDbClient.create(SpendUtils.generateForUser(user.getUsername()));
        Assertions.assertNotNull(spend.getId());
    }

    @Test
    void shouldGetSpendByIdTest(@CreateNewUser UserModel user) {
        var spend = spendDbClient.create(SpendUtils.generateForUser(user.getUsername()));
        var foundedSpend = spendDbClient.findById(spend.getId()).orElse(new SpendJson());
        assertEquals(spend, foundedSpend);
    }

    @Test
    void shouldGetSpendByUsernameAndDescriptionTest(@CreateNewUser UserModel user) {
        var spend = spendDbClient.create(SpendUtils.generateForUser(user.getUsername()));
        var foundedSpends = spendDbClient.findByUsernameAndDescription(
                        user.getUsername(),
                        spend.getDescription());
        assertTrue(foundedSpends.contains(spend));
    }

    @Test
    void shouldGetAllSpendsByUsernameTest(@CreateNewUser UserModel user) {

        var spend1 = spendDbClient.create(SpendUtils.generateForUser(user.getUsername()));
        var spend2 = spendDbClient.create(SpendUtils.generateForUser(user.getUsername()));
        List<SpendJson> categories = spendDbClient.findAllByUsername(user.getUsername());

        assertTrue(categories.contains(spend1));
        assertTrue(categories.contains(spend2));

    }

    @Test
    void shouldRemoveSpendTest(@CreateNewUser UserModel user) {
        SpendJson spend = spendDbClient.create(SpendUtils.generateForUser(user.getUsername()));
        spendDbClient.delete(spend);
        assertEquals(0, spendDbClient.findAllByUsername(user.getUsername()).size());
    }

}
