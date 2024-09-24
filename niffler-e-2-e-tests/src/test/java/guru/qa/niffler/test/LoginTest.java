package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.enums.ErrorValuesEnum.USER_CREDENTIAL_ERROR;
import static guru.qa.niffler.testData.DataConstant.MAIN_PASSWORD;
import static guru.qa.niffler.testData.DataConstant.MAIN_USER;

@ExtendWith(BrowserExtension.class)
@Feature("UI:Авторизация пользователя в системе")
public class LoginTest extends BaseTest {

  @Test
  @Description("Успешная авторизация в системе")
  void successLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(MAIN_USER, MAIN_PASSWORD)
        .checkSuccessLogin();
  }

  @Test
  @Description("Негативный сценарий:Успешная авторизация в системе")
  void errorLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(MAIN_USER, "ErrorPassword")
        .checkLoginErrorText(USER_CREDENTIAL_ERROR.getDescription());
  }
}