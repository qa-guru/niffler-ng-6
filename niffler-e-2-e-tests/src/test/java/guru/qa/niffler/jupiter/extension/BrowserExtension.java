package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.niffler.jupiter.converter.Browsers;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

public class BrowserExtension implements
    BeforeEachCallback,
    AfterEachCallback,
    TestExecutionExceptionHandler,
    LifecycleMethodExecutionExceptionHandler {

  private static final ThreadLocal<SelenideDriver> driver = new ThreadLocal<>();

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (driver.get().hasWebDriverStarted()) {
      driver.get().close();
    }
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Method requiredTestMethod = context.getRequiredTestMethod();
    if(requiredTestMethod.isAnnotationPresent(EnumSource.class)) {
      String name = context.getDisplayName();

      SelenideConfig selenideConfig = name.contains(Browsers.CHROME.name())
              ? Browsers.CHROME.driver()
              : Browsers.FIREFOX.driver();

      driver.set(new SelenideDriver(selenideConfig));
    }

    driver.set(new SelenideDriver(Browsers.CHROME.driver()));

    SelenideLogger.addListener("Allure-selenide", new AllureSelenide()
        .savePageSource(false)
        .screenshots(false)
    );
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  @Override
  public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  @Override
  public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  private static void doScreenshot() {
    if (driver.get().hasWebDriverStarted()) {
      Allure.addAttachment(
          "Screen on fail, with  driver: " + driver.get().getSessionId(),
          new ByteArrayInputStream(
              ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES)
          )
      );
    }
  }
}
