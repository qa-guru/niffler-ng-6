package guru.qa.nifler.jupiter;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;


public interface SuiteExtension extends BeforeAllCallback {

  /*
  1) Быть уверенным, что SuiteExtension выполняется перед каждым тестовым классом
  2) Если мы выполним какой-то код перед загрузкой самого первого тестового класса, то это и будет beforeSuite()
  3) При этом для 2,3 и т.д. (до N) тестовых классов, больше не будем вызывать beforeSuite()
  3) Когда все тесты завершаться, вызовем afterSuite()
   */

  @Override
  default void beforeAll(ExtensionContext context) throws Exception {
    final ExtensionContext rootContext = context.getRoot();
    context.getStore(GLOBAL)
        .getOrComputeIfAbsent(
            this.getClass(),
            key -> { // Попадаем только в самый первый раз
              beforeSuite(rootContext);
              return new CloseableResource() {
                @Override
                public void close() throws Throwable {
                  afterSuite();
                }
              };
            }
        );
  }

  default void beforeSuite(ExtensionContext context) {}

  default void afterSuite() {}
}
