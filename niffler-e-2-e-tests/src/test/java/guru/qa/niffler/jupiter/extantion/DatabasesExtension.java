package guru.qa.niffler.jupiter.extantion;

import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.tpl.Connections;

public class DatabasesExtension implements SuiteExtension {
    @Override
    public void afterSuite() {
        Connections.closeAllConnections();
        EntityManagers.closeAllEmfs();
    }
}
