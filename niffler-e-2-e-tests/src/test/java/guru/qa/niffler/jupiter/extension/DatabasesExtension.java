package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.Databases;
import guru.qa.niffler.jupiter.SuiteExtension;

public class DatabasesExtension implements SuiteExtension {
  @Override
  public void afterSuite(){
    Databases.closeAllConnections();
  }
}
