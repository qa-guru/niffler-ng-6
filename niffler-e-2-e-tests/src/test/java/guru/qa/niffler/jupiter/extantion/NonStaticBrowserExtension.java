package guru.qa.niffler.jupiter.extantion;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class NonStaticBrowserExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        LifecycleMethodExecutionExceptionHandler {



    private final List<SelenideDriver> drivers = new ArrayList<>();

    public List<SelenideDriver> drivers() {
        return drivers;
    }

    private static final ThreadLocal<SelenideDriver> driverThreadLocal = new ThreadLocal<>();

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        for (SelenideDriver driver : drivers) {
            driverThreadLocal.set(driver);
            if (driverThreadLocal.get().hasWebDriverStarted()) {
                driverThreadLocal.get().close();
            }
            driverThreadLocal.remove();
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

    private void doScreenshot() {
        for (SelenideDriver driver : drivers) {
            driverThreadLocal.set(driver);
            if (driverThreadLocal.get().hasWebDriverStarted()) {
                Allure.addAttachment(
                        "Screen on fail" + driverThreadLocal.get().getSessionId(),
                        new ByteArrayInputStream(
                                ((TakesScreenshot) driverThreadLocal.get().getWebDriver()).getScreenshotAs(OutputType.BYTES)
                        )
                );
            }
        }
    }
}