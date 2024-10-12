package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Creating new category:<br/>
 * <b>* name</b>: Default = "". If you want to use default value set <b><i>generateNameStatus</i></b> = false.<br/><br/>
 * <b>* username</b>: Default = "". <b><u>Test does not have</u></b> annotation @CreateNewUser when use this param.<br/><br/>
 * <b>* archived</b>: Default = false. If you want to get random status set <b><i>notGenerateIsArchived</i></b> = false.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(CategoryExtension.class)
public @interface Category {
    String name() default "";

    boolean isArchived() default false;

    boolean generateIsArchived() default false;
}