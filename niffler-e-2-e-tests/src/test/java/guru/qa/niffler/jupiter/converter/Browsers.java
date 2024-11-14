package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideConfig;

public enum Browsers {
    CHROME("chrome"),
    FIREFOX("firefox");

    private final String name;

    Browsers(String name) {
        this.name = name;
    }

    public SelenideConfig driver() {
        return new SelenideConfig()
                .browser(name)
                .timeout(5000L)
                .pageLoadStrategy("eager");
    }
}
