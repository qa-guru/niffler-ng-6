package guru.qa.niffler.jupiter.annotations;

import guru.qa.niffler.jupiter.extensions.CategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(CategoryExtension.class)
public @interface Category {
    String username() default "";

    boolean archived();
}
