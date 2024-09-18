package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.AddCategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(AddCategoryExtension.class)
public  @interface Category {
    String username();
    boolean isArchive();
    String name() default "";
}