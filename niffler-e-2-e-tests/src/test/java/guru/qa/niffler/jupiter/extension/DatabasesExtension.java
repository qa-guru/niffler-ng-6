package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.Databases;

public class DatabasesExtension implements SuiteExtension {
  @Override
  public void afterSuite() {
    Databases.closeAllConnections();
  }
}
