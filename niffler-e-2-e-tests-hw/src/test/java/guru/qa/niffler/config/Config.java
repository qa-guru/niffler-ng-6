package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String authUrl();

    String authJdbcUrl();

    String currencyJdbcUrl();

    String frontUrl();

    String spendUrl();

    String spendJdbcUrl();

    String userdataUrl();

    String userdataJdbcUrl();

    default String gitHubUrl() {
        return "https://api.github.com/";
    }

}