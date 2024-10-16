package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.jdbc.SpendDbClient;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SpendSpringJdbcTest {

    @Test
    void shouldCreateNewSpendTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldGetSpendByIdTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldGetSpendByUsernameAndDescriptionTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldGetAllSpendsByUsernameTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldRemoveSpendTest(@CreateNewUser UserModel user) {

    }

}
