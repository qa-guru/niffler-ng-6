package guru.qa.niffler.config;

enum DockerConfig implements Config {
    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:80/";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-auth";
    }

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8090/";
    }

    @Override
    public String userdataUrl() {
        return "http://127.0.0.1:8089/";
    }

    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-userdata";
    }

    @Override
    public String spendUrl() {
        return "http://127.0.0.1:8093/";
    }

    @Override
    public String spendJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-spend";
    }

    @Override
    public String currencyJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/niffler-currency";
    }

    @Override
    public String currencyGrpcAddress() {
        return "";
    }

    @Override
    public String ghUrl() {
        return "https://api.github.com/";
    }
}