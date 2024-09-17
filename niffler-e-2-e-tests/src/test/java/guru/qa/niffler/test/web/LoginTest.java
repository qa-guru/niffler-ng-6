package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.Pages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@ExtendWith(BrowserExtension.class)
public class LoginTest extends Pages {
  String userData = "kisa";

  private static final Config CFG = Config.getInstance();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    openLoginPage();
    loginPage.login(userData, userData);
    mainPage.mainPageAfterLoginCheck();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    openLoginPage();
    loginPage.login(userData, "kiss");
    loginPage.loginErrorCheck("Bad credentials");
  }

  @BeforeEach
  public void openLoginPage() {
    step("Открыть страницу авторизации", () -> {
      open(CFG.frontUrl(), LoginPage.class);
    });
  }
}
