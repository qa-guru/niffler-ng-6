package guru.qa.niffler.jupiter.annotations.meta;

import guru.qa.niffler.jupiter.extensions.BrowserExtension;
import guru.qa.niffler.jupiter.extensions.UserQueueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        BrowserExtension.class,
        UserQueueExtension.class
})
public @interface WebTest {
}