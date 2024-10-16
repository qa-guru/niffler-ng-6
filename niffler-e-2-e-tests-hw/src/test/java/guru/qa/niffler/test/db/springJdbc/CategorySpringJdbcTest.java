package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.jdbc.CategoryDbClient;
import guru.qa.niffler.utils.CategoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CategorySpringJdbcTest {

    @Test
    void shouldCreateNewCategoryTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldGetCategoryByIdTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldGetCategoryByUsernameAndNameTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldGetAllSpendsByUsernameTest(@CreateNewUser UserModel user) {

    }

    @Test
    void shouldRemoveCategoryTest(@CreateNewUser UserModel user) {

    }

}
