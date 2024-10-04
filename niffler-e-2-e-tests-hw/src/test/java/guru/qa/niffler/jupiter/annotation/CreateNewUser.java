package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CreateNewUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(CreateNewUserExtension.class)
public @interface CreateNewUser {
    String username() default "";

    String password() default "";

    String fullName() default "";

    String avatar() default "";
}
