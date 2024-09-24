package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import io.qameta.allure.Story;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

@ExtendWith(BrowserExtension.class)
@Story("Тестирование niffler-ng 6 поток")
public class BaseTest {

  protected static final Config CFG = Config.getInstance();
  protected static final Random randomizer = new Random();
}