package guru.qa.niffler.data.tpl;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DataSources  {
    private DataSources() {
    }

    private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();

    public static DataSource dataSource(String jdbcUrl) {
        return datasources.computeIfAbsent(
                jdbcUrl,
                key -> {
                    AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
                    String uniqueId = StringUtils.substringAfter(jdbcUrl, "5432/");
                    ds.setUniqueResourceName(uniqueId);
                    ds.setUniqueResourceName("org.postgresql.xa.PGXADataSource");
                    Properties props = new Properties();
                    props.put("URL", jdbcUrl);
                    props.put("user", "postgres");
                    props.put("password", "secret");
                    ds.setXaProperties(props);
                    ds.setPoolSize(3);
                    ds.setMaxPoolSize(10);
                    return ds;
                }
        );
    }
}
