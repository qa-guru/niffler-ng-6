package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.instance;
  }

  String frontUrl();
  String spendUrl();
}
