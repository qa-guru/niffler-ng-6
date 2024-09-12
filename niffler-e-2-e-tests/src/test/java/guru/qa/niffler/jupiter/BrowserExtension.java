package guru.qa.niffler.jupiter;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class BrowserExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        LifecycleMethodExecutionExceptionHandler {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
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
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.addAttachment(
                    "Screen on fail",
                    new ByteArrayInputStream(
                            ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)
                    )
            );
        }
    }
}
