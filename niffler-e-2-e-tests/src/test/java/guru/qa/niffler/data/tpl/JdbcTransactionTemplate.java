package guru.qa.niffler.data.tpl;

import guru.qa.niffler.data.jdbc.Connections;
import guru.qa.niffler.data.jdbc.JdbcConnectionHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

@ParametersAreNonnullByDefault
public class JdbcTransactionTemplate {

  private final JdbcConnectionHolder holder;
  private final AtomicBoolean closeAfterAction = new AtomicBoolean(true);

  public JdbcTransactionTemplate(String jdbcUrl) {
    this.holder = Connections.holder(jdbcUrl);
  }

  @Nonnull
  public JdbcTransactionTemplate holdConnectionAfterAction() {
    this.closeAfterAction.set(false);
    return this;
  }

  @Nullable
  public <T> T execute(Supplier<T> action, int isolationLvl) {
    Connection connection = null;
    try {
      connection = holder.connection();
      connection.setTransactionIsolation(isolationLvl);
      connection.setAutoCommit(false);
      T result = action.get();
      connection.commit();
      connection.setAutoCommit(true);
      return result;
    } catch (Exception e) {
      if (connection != null) {
        try {
          connection.rollback();
          connection.setAutoCommit(true);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      throw new RuntimeException(e);
    } finally {
      if (closeAfterAction.get()) {
        holder.close();
      }
    }
  }

  @Nullable
  public <T> T execute(Supplier<T> action) {
    return execute(action, TRANSACTION_READ_COMMITTED);
  }
}
