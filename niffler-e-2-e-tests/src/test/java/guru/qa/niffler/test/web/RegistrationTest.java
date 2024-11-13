package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.converter.Browsers;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegistrationTest {

  @ParameterizedTest
  @EnumSource(Browsers.class)
  void shouldRegisterNewUser(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
    String newUsername = randomUsername();
    String password = "12345";
    driver.open(LoginPage.URL);
    new LoginPage(driver)
        .doRegister()
        .fillRegisterPage(newUsername, password, password)
        .successSubmit()
        .fillLoginPage(newUsername, password)
        .submit(new MainPage(driver))
        .checkThatPageLoaded();
  }

  @ParameterizedTest
  @EnumSource(Browsers.class)
  void shouldNotRegisterUserWithExistingUsername(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
    String existingUsername = "duck";
    String password = "12345";
    driver.open(LoginPage.URL);
    LoginPage loginPage = new LoginPage(driver);
    loginPage.doRegister()
        .fillRegisterPage(existingUsername, password, password)
        .errorSubmit();
    loginPage.checkError("Username `" + existingUsername + "` already exists");
  }

  @ParameterizedTest
  @EnumSource(Browsers.class)
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
    String newUsername = randomUsername();
    String password = "12345";
    driver.open(LoginPage.URL);
    LoginPage loginPage = new LoginPage(driver);
    loginPage.doRegister()
        .fillRegisterPage(newUsername, password, "bad password submit")
        .errorSubmit();
    loginPage.checkError("Passwords should be equal");
  }
}
