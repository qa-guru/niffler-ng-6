package guru.qa.niffler.test;

import guru.qa.niffler.enums.CurrencyValuesEnum;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.AuthAuthorityDbClient;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UdDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

public class HWTest {

  @Test
  void spendTest() {
    final SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spendJson = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                randomCategoryName(),
                "max",
                false
            ),
            CurrencyValuesEnum.RUB,
            100.0,
            "test desc",
            "max"
        )
    );
    System.out.println(spendDbClient.findSpendById(spendJson.id()));
    spendDbClient.deleteSpend(spendJson);
  }

  @Test
  void categoryTest() {
    final CategoryDbClient categoryDbClient = new CategoryDbClient();

    CategoryJson categoryJson = categoryDbClient.createCategory(
        new CategoryJson(
            null,
            "test-cat-name-3",
            "max",
            false
        )
    );
    System.out.println(categoryDbClient.findCategoryById(categoryJson.id()));
    categoryDbClient.deleteCategory(categoryJson.id());
  }

  @Test
  void userdataTest() {
    final UdDbClient udDbClient = new UdDbClient();

    UserJson userJson = udDbClient.createUserdata(
        new UserJson(
            UUID.fromString("d1e82876-9075-11ef-b55c-0242ac110002"),
            "test-userName",
            "maxim",
            "hud",
            "Max Hud",
            CurrencyValuesEnum.RUB,
            "photo",
            "smallPhoto"
        )
    );
  }

  @Test
  void springJDBCTest() {
    final UdDbClient udDbClient = new UdDbClient();

    UserJson userJson = udDbClient.createUserSpringJDBC(
        new UserJson(
            null,
            randomUserName(),
            null,
            null,
            null,
            CurrencyValuesEnum.RUB,
            null,
            null
        )
    );
    System.out.println(userJson);
  }

  @Test
  void findAllTest() {
    System.out.println(new AuthAuthorityDbClient().findAll());
    System.out.println(new AuthUserDbClient().findAll());
    System.out.println(new CategoryDbClient().findAll());
    System.out.println(new SpendDbClient().findAll());
    System.out.println(new UdDbClient().findAll());
  }
}
