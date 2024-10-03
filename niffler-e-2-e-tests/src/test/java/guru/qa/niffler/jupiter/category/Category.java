package guru.qa.niffler.jupiter.category;

import guru.qa.niffler.jupiter.spend.SpendExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(SpendExtension.class)
public @interface Category {
  String name();
  String username();
  boolean isArchived();
}