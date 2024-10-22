package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Epic;

import java.util.Random;

@Epic("Тестирование niffler-ng 6 поток")
public class BaseTest {
  protected static final Config CFG = Config.getInstance();
  protected static final Random randomizer = new Random();

  protected final MainPage logIntoSystem(String userName, String password) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(userName, password);
    return new MainPage();
  }
}