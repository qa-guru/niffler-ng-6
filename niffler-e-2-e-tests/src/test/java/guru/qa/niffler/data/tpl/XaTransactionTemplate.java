package guru.qa.niffler.data.tpl;

import com.atomikos.icatch.jta.UserTransactionImp;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class XaTransactionTemplate {

  private final JdbcConnectionHolders holders;
  private final AtomicBoolean closeAfterAction = new AtomicBoolean(true);

  public XaTransactionTemplate(String... jdbcUrl) {
    this.holders = Connections.holders(jdbcUrl);
  }

  public XaTransactionTemplate holdConnectionAfterAction() {
    this.closeAfterAction.set(false);
    return this;
  }

  public <T> T execute(Supplier<T>... actions) {
    UserTransaction ut = new UserTransactionImp();
    try {
      ut.begin();
      T result = null;
      for (Supplier<T> action : actions) {
        result = action.get();
      }
      ut.commit();
      return result;
    } catch (Exception e) {
      try {
        ut.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    } finally {
      if (closeAfterAction.get()) {
        holders.close();
      }
    }
  }
}
