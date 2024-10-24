package guru.qa.niffler.config;

enum DockerConfig implements Config {
    INSTANCE;

    @Override
    public String frontUrl() {
        return "";
    }

    @Override
    public String authUrl() {
        return "";
    }

    @Override
    public String authJdbcUrl() {
        return "";
    }

    @Override
    public String gatewayUrl() {
        return "";
    }

    @Override
    public String userdataUrl() {
        return "";
    }

    @Override
    public String userdataJdbcUrl() {
        return "";
    }

    @Override
    public String spendUrl() {
        return "";
    }

    @Override
    public String spendJdbcUrl() {
        return "";
    }

    @Override
    public String currencyJdbcUrl() {
        return "";
    }

    @Override
    public String ghUrl() {
        return "";
    }
}