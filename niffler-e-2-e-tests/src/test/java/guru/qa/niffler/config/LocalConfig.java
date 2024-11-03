package guru.qa.niffler.config;

enum LocalConfig implements Config {
  INSTANCE;

  @Override
  public String frontUrl() {
    return "http://niffler.lan:3000/";
  }

  @Override
  public String authUrl() {
    return "http://niffler.lan:9000/";
  }

  @Override
  public String authJdbcUrl() {
    return "jdbc:postgresql://niffler.lan:5432/niffler-auth";
  }

  @Override
  public String gatewayUrl() {
    return "http://niffler.lan:8090/";
  }

  @Override
  public String userdataUrl() {
    return "http://niffler.lan:8089/";
  }

  @Override
  public String userdataJdbcUrl() {
    return "jdbc:postgresql://niffler.lan:5432/niffler-userdata";
  }

  @Override
  public String spendUrl() {
    return "http://niffler.lan:8093/";
  }

  @Override
  public String spendJdbcUrl() {
    return "jdbc:postgresql://niffler.lan:5432/niffler-spend";
  }

  @Override
  public String currencyJdbcUrl() {
    return "jdbc:postgresql://niffler.lan:5432/niffler-currency";
  }

  @Override
  public String ghUrl() {
    return "https://api.github.com/";
  }

}
