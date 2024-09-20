package guru.qa.niffler.jupiter.extensions;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {
    /*
    1. Быть уверенными, что SuiteExtension будет выполняться перед каждым тестовым классом
    2. Если мы выполним какой-то код перед загрузкой самого первого тестового класса, то это и будет beforeSuite()
    3. При этом, для 2, 3 и т.д (до N тестовых классов, больше не будем вызывать beforeSuite)
    4. Когда все тест завершаться, вызовем afterSuite()
     */
    @Override
    default void beforeAll(ExtensionContext context) throws Exception {
        ExtensionContext rootContext = context.getRoot();
        rootContext.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(
                        this.getClass(),
                        key -> { // попадаем только в самый первый раз
                            beforeSuite(rootContext);
                            return new ExtensionContext.Store.CloseableResource() {
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
