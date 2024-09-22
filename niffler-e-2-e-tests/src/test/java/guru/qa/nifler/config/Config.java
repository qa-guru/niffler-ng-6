package guru.qa.nifler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.instance;
  }

  String frontUrl();
  String spendUrl();
}
