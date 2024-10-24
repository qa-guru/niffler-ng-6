package guru.qa.niffler.data.tpl;

import java.util.List;

public class JdbcConnectionHolders implements AutoCloseable {

  private final List<JdbcConnectionHolder> holders;

  public JdbcConnectionHolders(List<JdbcConnectionHolder> holders) {
    this.holders = holders;
  }

  @Override
  public void close() {
    holders.forEach(JdbcConnectionHolder::close);
  }
}
