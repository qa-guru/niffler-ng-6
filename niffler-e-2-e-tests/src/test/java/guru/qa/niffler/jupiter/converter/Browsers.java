package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideConfig;

public enum Browsers {
    CHROME {
        public SelenideConfig driver() {
            return new SelenideConfig()
                    .browser("chrome")
                    .timeout(5000L)
                    .pageLoadStrategy("eager");
        }
    },
    FIREFOX {
        public SelenideConfig driver() {
            return new SelenideConfig()
                    .browser("firefox")
                    .timeout(5000L)
                    .pageLoadStrategy("eager");
        }
    };

    public abstract SelenideConfig driver();
}
