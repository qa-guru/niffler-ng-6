package guru.qa.niffler.jupiter;

import guru.qa.niffler.enums.CurrencyValuesEnum;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({CreateSpendingExtension.class, SpendingResolverExtension.class})
public @interface Spend {
  String category();

  String description();

  String username();

  double amount();

  CurrencyValuesEnum currency();
}