package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestMethodContextExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Holder.INSTANCE.set(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Holder.INSTANCE.remove();
    }

    private enum Holder {
        INSTANCE;

        private final ThreadLocal<ExtensionContext> holder = new ThreadLocal<>();

        public void set(ExtensionContext context) {
            holder.set(context);
        }

        public ExtensionContext get() {
            return holder.get();
        }

        public void remove() {
            holder.remove();
        }
    }

    public static ExtensionContext context() {
        return Holder.INSTANCE.get();
    }
}