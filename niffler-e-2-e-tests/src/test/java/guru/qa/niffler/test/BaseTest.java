package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

@Epic("Тестирование niffler-ng 6 поток")
@ExtendWith(BrowserExtension.class)
public class BaseTest {
  protected static final Config CFG = Config.getInstance();
  protected static final Random randomizer = new Random();
}