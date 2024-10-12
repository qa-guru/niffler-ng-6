package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();
  private static final Faker faker = new Faker();

  @Test
  void shouldRegisterNewUser() {
    String newUsername = faker.name().username();
    String password = "12345";
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doRegister()
        .fillRegisterPage(newUsername, password, password)
        .successSubmit()
        .successLogin(newUsername, password)
        .checkThatPageLoaded();
  }

  @Test
  void shouldNotRegisterUserWithExistingUsername() {
    String existingUsername = "duck";
    String password = "12345";

    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    loginPage.doRegister()
        .fillRegisterPage(existingUsername, password, password)
        .submit();
    loginPage.checkError("Username `" + existingUsername + "` already exists");
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    String newUsername = faker.name().username();
    String password = "12345";

    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    loginPage.doRegister()
        .fillRegisterPage(newUsername, password, "bad password submit")
        .submit();
    loginPage.checkError("Passwords should be equal");
  }
}
