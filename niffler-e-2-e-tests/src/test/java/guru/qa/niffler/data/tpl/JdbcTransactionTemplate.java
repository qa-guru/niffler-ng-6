package guru.qa.niffler.data.tpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class JdbcTransactionTemplate {
    private final JdbcConnectionHolder holder;
    private final AtomicBoolean closeAfterAction = new AtomicBoolean(true);

    public JdbcTransactionTemplate(String jdbcUrl) {
        this.holder = Connections.holder(jdbcUrl);
    }

    public <T> T transaction(Supplier<T> action, int isolationLevel) {
        Connection connection = null;
        try {
            connection = holder.connection();
            connection.setTransactionIsolation(isolationLevel);
            connection.setAutoCommit(false);
            T result = action.get();
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
        } finally {
            if(closeAfterAction.get()) {
                holder.close();;
            }
        }
    }

    public <T> T transaction(Supplier<T> action, String jdbcUrl) {
        return transaction(action, Connection.TRANSACTION_READ_COMMITTED);
    }

    public JdbcTransactionTemplate holdConnectionAfterAction(){
        this.closeAfterAction.set(false);
        return this;
    }
}
