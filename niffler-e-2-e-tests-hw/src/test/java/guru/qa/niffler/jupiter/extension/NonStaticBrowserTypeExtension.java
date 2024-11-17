package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.niffler.utils.NonStaticSelenideUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.ByteArrayInputStream;

@ParametersAreNonnullByDefault
public class NonStaticBrowserTypeExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        LifecycleMethodExecutionExceptionHandler {

    private static final ThreadLocal<SelenideDriver> threadSafeDriver = new ThreadLocal<>();

    private SelenideDriver drivers() {
        return threadSafeDriver.get();
    }

    private static void doScreenshot() {

        if (WebDriverRunner.hasWebDriverStarted())
            Allure.addAttachment(
                    "Screen on fail",
                    new ByteArrayInputStream(
                            ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)
                    )
            );

    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        SelenideLogger.addListener(
                "Allure-selenide",
                new AllureSelenide()
                        .savePageSource(false)
                        .screenshots(false)
        );
        Configuration.pageLoadStrategy = PageLoadStrategy.EAGER.toString();
        Configuration.timeout = 5000;
        Configuration.pageLoadTimeout = 10000;
        threadSafeDriver.set(new SelenideDriver(NonStaticSelenideUtils.chromeConfig()));
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenshot();
        throw throwable;
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenshot();
        throw throwable;
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (threadSafeDriver.get().hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenshot();
        throw throwable;
    }

}