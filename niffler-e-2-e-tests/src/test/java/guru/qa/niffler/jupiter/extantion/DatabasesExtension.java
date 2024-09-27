package guru.qa.niffler.jupiter.extantion;

import guru.qa.niffler.data.Databases;

public class DatabasesExtension implements SuiteExtension {
    @Override
    public void afterSuite() {
        Databases.closeAllConnection();
    }
}
