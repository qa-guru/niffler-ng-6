package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String ghUrl();

  String authUrl();

  String gatewayUrl();

  String userdataUrl();
}
