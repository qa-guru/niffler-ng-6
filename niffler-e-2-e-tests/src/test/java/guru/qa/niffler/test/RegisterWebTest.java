package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.enums.ErrorValuesEnum.PASSWORD_NOT_EQUALS;
import static guru.qa.niffler.enums.ErrorValuesEnum.USER_ALREADY_EXIST;
import static guru.qa.niffler.testData.DataConstant.MAIN_USER;

@ExtendWith(BrowserExtension.class)
@Feature("UI:Регистрация пользователя в системе")
public class RegisterWebTest extends BaseTest {

  @Test
  @Description("Регистрация нового пользователя")
  void registerNewUser() {
    final String userName = "max" + randomizer.nextInt(500);
    final String userPassword = "maxPassword" + randomizer.nextInt(5);

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .goToRegistrationPage()
        .registerNewUser(userName, userPassword)
        .doLogin(userName, userPassword)
        .checkSuccessLogin();
  }

  @Test
  @Description("Негативный сценарий: Регистрация ранее зарегистрированного пользователя")
  void registerExistingUser() {
    final String userName = MAIN_USER;
    final String userPassword = "maxPassword" + randomizer.nextInt(5);
    final String errorText = String.format(USER_ALREADY_EXIST.getDescription(), userName);

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .goToRegistrationPage()
        .setNewUserDataField(userName, userPassword, userPassword)
        .submitRegistration()
        .checkUsernameErrorText(errorText);
  }

  @Test
  @Description("Негативный сценарий: Проверка появления ошибки различия Password и Confirm password при регистрации пользователя")
  void checkPasswordAndConfirmPasswordNotEqual() {
    final String userName = "max";
    final String userPassword = "maxPassword" + randomizer.nextInt(5);
    final String submitPassword = "maxPassword";
    final String errorText = PASSWORD_NOT_EQUALS.getDescription();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .goToRegistrationPage()
        .setNewUserDataField(userName, userPassword, submitPassword)
        .submitRegistration()
        .checkPasswordErrorText(errorText);
  }
}