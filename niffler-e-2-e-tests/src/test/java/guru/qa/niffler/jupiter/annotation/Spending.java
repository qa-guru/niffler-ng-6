package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.rest.CurrencyValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Spending {
  String category() default "";

  String description();

  double amount();

  CurrencyValues currency() default CurrencyValues.RUB;
}
