package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String spendJdbcUrl();

  String authUrl();

  String authJdbcUrl();

  String gatewayUtl();

  String userdataUrl();

  String userdataJdbcUrl();

  String currencyJdbcUrl();

  String ghUrl();
}
