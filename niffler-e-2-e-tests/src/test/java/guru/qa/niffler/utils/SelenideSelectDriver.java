package guru.qa.niffler.utils;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;

public class SelenideSelectDriver {

    public SelenideDriver getDriver(SelenideConfigBrowser browser) {
        SelenideDriver driver = switch (browser) {
            case CHROME -> new SelenideDriver(new SelenideConfig()
                    .browser("chrome")
                    .pageLoadStrategy("eager")
                    .timeout(5000L));
            case FIREFOX -> new SelenideDriver(new SelenideConfig()
                    .browser("firefox")
                    .pageLoadStrategy("eager")
                    .timeout(5000L));
        };
        return driver;
    }
}
