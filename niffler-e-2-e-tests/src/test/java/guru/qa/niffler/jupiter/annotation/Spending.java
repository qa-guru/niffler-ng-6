package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CreateSpendingExtension;
import guru.qa.niffler.jupiter.extension.SpendingResolverExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({CreateSpendingExtension.class, SpendingResolverExtension.class})
public @interface Spending {
  String username();

  String category();

  String description();

  double amount();
}
