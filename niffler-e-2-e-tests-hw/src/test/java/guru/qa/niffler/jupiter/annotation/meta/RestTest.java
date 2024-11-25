package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.extension.*;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
        AllureJunit5.class,
        CreateNewUserExtension.class,
        CategoryExtension.class,
        SpendingExtension.class,
        FriendshipExtension.class,
        ApiLoginExtension.class
})
public @interface RestTest {
}
