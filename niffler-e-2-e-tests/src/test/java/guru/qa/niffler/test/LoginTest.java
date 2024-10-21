package guru.qa.niffler.test;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.enums.ErrorValuesEnum.USER_CREDENTIAL_ERROR;
import static guru.qa.niffler.testData.DataConstant.MAIN_PASSWORD;
import static guru.qa.niffler.testData.DataConstant.MAIN_USER;

@Feature("UI:Авторизация пользователя в системе")
public class LoginTest extends BaseTest {

  @Test
  @Description("Успешная авторизация в системе")
  void successLogin() {
    logIntoSystem(MAIN_USER, MAIN_PASSWORD)
        .checkSuccessLogin();
  }

  @Test
  @Description("Негативный сценарий:Успешная авторизация в системе")
  void errorLogin() {
    logIntoSystem(MAIN_USER, "ErrorPassword")
        .checkLoginErrorText(USER_CREDENTIAL_ERROR.getDescription());
  }
}