package guru.qa.niffler.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
    private Databases() {

    }

    private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();
    private static final Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

    public record XAFunction<T>(Function<Connection, T> function, String jdbcUrl) {
    }

    public record XAConsumer(Consumer<Connection> function, String jdbcUrl) {
    }

    private static DataSource dataSource(String jdbcUrl) {
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
                    ds.setPoolSize(10);
                    return ds;
                }
        );
    }

    public static Connection connection(String jdbcUrl) throws SQLException {
        return threadConnections.computeIfAbsent(
                Thread.currentThread().threadId(),
                key -> {
                    try {
                        return new HashMap<>(Map.of(
                                jdbcUrl,
                                dataSource(jdbcUrl).getConnection()
                        ));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).computeIfAbsent(
                jdbcUrl,
                key -> {
                    try {
                        return dataSource(jdbcUrl).getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public static void closeAllConnections() {
        for (Map<String, Connection> connectionMap : threadConnections.values()) {
            for (Connection connection : connectionMap.values()) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {

                }
            }
        }
    }

    public static <T> T transaction(Function<Connection, T> function, String jdbcUrl, int isolationLevel) {
        Connection connection = null;
        try {
            connection = connection(jdbcUrl);
            connection.setTransactionIsolation(isolationLevel);
            connection.setAutoCommit(false);
            T result = function.apply(connection);
            connection.commit();
            connection.setAutoCommit(true);
            return result;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public static <T> T xaTransaction(int isolationLevel, XAFunction<T>... actions) {
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            T result = null;
            for (XAFunction<T> action : actions) {
                var connection = connection(action.jdbcUrl);
                connection.setTransactionIsolation(isolationLevel);
                result = action.function.apply(connection);
            }
            ut.commit();
            return result;
        } catch (Exception e) {
            try {
                ut.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException();
            }
            throw new RuntimeException();
        }
    }

    public static void transaction(Consumer<Connection> function, String jdbcUrl, int isolationLevel) {
        Connection connection = null;
        try {
            connection = connection(jdbcUrl);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(isolationLevel);
            function.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public static void xaTransaction(int isolationLevel, XAConsumer... actions) {
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            for (XAConsumer action : actions) {
                var connection = connection(action.jdbcUrl);
                connection.setTransactionIsolation(isolationLevel);
                action.function.accept(connection);
            }
            ut.commit();
        } catch (Exception e) {
            try {
                ut.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException();
            }
            throw new RuntimeException();
        }
    }
}
