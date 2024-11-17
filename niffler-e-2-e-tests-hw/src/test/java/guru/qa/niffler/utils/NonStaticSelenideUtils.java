package guru.qa.niffler.utils;

import com.codeborne.selenide.SelenideConfig;
import guru.qa.niffler.enums.NonStaticBrowserType;

public class NonStaticSelenideUtils {

    private static SelenideConfig defaultConfig(){
        return new SelenideConfig()
                .browser(NonStaticBrowserType.chrome.name())
                .pageLoadStrategy("eager")
                .pageLoadTimeout(10000)
                .timeout(5000);
    }

    public static SelenideConfig chromeConfig(){
        return defaultConfig();
    }

    public static SelenideConfig firefoxConfig(){
        return defaultConfig().browser(NonStaticBrowserType.firefox.name());
    }

    public static SelenideConfig operaConfig(){
        return defaultConfig().browser(NonStaticBrowserType.opera.name());
    }

}
