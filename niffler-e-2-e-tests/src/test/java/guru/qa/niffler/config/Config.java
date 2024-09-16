package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }
  
  static Config getInstanceDocker() {
    return DockerConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String ghUrl();
}
