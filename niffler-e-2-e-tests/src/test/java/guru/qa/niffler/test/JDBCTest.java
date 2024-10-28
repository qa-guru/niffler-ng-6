package guru.qa.niffler.test;

import guru.qa.niffler.enums.CurrencyValuesEnum;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UdDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class JDBCTest {

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
    System.out.println(spendDbClient.findAllByUsername("max"));
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
    System.out.println(categoryDbClient.findAllByUsername("max"));
    categoryDbClient.deleteCategory(categoryJson);
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
    System.out.println(udDbClient.findUserdataById(userJson.id()));
    System.out.println(udDbClient.findAllByUsername("test-userName"));
    udDbClient.deleteUserdata(userJson);
  }
}
