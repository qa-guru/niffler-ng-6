package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String authUrl();

    String frontUrl();

    String spendUrl();
}
