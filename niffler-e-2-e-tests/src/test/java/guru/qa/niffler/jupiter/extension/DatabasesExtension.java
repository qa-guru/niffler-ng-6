package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.jdbc.Connections;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.jpa.EntityManagers;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabasesExtension implements SuiteExtension {
    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate authJdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    private final JdbcTemplate spendJdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    private final JdbcTemplate userdataJdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));

    @Override
    public void beforeSuite(ExtensionContext context) {
        // Очищаем все таблицы перед началом тестов
        clearDatabaseTables();
    }

    @Override
    public void afterSuite() {
        Connections.closeAllConnections();
        EntityManagers.closeAllEmfs();
    }

    private void clearDatabaseTables() {
        // Очищаем таблицы базы данных niffler-spend
        spendJdbcTemplate.execute("TRUNCATE TABLE category, spend CASCADE;");

        // Очищаем таблицы базы данных niffler-userdata
        userdataJdbcTemplate.execute("TRUNCATE TABLE friendship, \"user\" CASCADE;");

        // Очищаем таблицы базы данных niffler-auth
        authJdbcTemplate.execute("TRUNCATE TABLE authority, \"user\" CASCADE;");
    }
}
