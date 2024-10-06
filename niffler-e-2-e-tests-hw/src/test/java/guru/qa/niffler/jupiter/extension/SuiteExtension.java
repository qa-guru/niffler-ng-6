package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {

    /**
     * Что нужно:
     * 1) Быть уверенным что данный Extension будет выполняться перед каждым тестом;
     * 2) Если мы выполним какой-то код перед загрузкой самого первого тестового класса, то это и будет beforeSuite();
     * 3) При этом, для 2, 3, ..., N тестовых классом, больше не будем вызывать beforeSuite();
     * 4) Когда все тесты будут выполнены, то вызовем afterSuite();
     */
    @Override
    default void beforeAll(ExtensionContext context) throws Exception {

        final ExtensionContext rootContext = context.getRoot();

        rootContext.getStore(ExtensionContext.Namespace.GLOBAL).getOrComputeIfAbsent(
                this.getClass(),
                key -> {
                    beforeSuite(context);
                    return (ExtensionContext.Store.CloseableResource) this::afterSuite;
                }
        );
    }

    default void beforeSuite(ExtensionContext context) {

    }

    default void afterSuite() {

    }
}