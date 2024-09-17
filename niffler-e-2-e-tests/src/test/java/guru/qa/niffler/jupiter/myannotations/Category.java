package guru.qa.niffler.jupiter.myannotations;

import guru.qa.niffler.jupiter.myextensions.CategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(CategoryExtension.class)
public @interface Category {
  String username();

  boolean archived();

}
