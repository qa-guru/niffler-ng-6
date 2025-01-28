package guru.qa.niffler.jupiter.annotation;

<<<<<<<< HEAD:niffler-e-2-e-tests/src/test/java/guru/qa/niffler/jupiter/annotation/Spending.java
import guru.qa.niffler.model.rest.CurrencyValues;
========
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;
>>>>>>>> hw-15-2:niffler-e-2-e-tests/src/test/java/guru/qa/niffler/jupiter/annotation/Category.java

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
<<<<<<<< HEAD:niffler-e-2-e-tests/src/test/java/guru/qa/niffler/jupiter/annotation/Spending.java
public @interface Spending {
  String category() default "";

  String description();

  double amount();

  CurrencyValues currency() default CurrencyValues.RUB;
}
========

public @interface Category {
    String name() default "";
    boolean archived() default false;
}
>>>>>>>> hw-15-2:niffler-e-2-e-tests/src/test/java/guru/qa/niffler/jupiter/annotation/Category.java
