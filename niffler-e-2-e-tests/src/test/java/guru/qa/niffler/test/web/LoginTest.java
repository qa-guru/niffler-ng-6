package guru.qa.niffler.test.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class LoginTest {

  @RegisterExtension
  private final BrowserExtension browserExtension = new BrowserExtension();
  private final SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);

  @User
  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
    browserExtension.drivers().add(driver);

    driver.open(LoginPage.URL);
    new LoginPage(driver)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    SelenideDriver firefox = new SelenideDriver(SelenideUtils.firefoxConfig);

    browserExtension.drivers().addAll(List.of(driver, firefox));

    driver.open(LoginPage.URL);
    firefox.open(LoginPage.URL);
    new LoginPage(driver)
        .fillLoginPage(randomUsername(), "BAD")
        .submit(new LoginPage(driver))
        .checkError("Bad credentials!");

    firefox.$(".logo-section__text").should(Condition.text("Niffler!"));
  }
}
