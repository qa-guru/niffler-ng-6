package guru.qa.niffler.config;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String authUrl();

    String authJdbcUrl();

    String currencyJdbcUrl();

    String frontUrl();

    String gatewayUrl();

    String spendUrl();

    String spendJdbcUrl();

    String userdataUrl();

    String userdataJdbcUrl();

    default String gitHubUrl() {
        return "https://api.github.com/";
    }

}