package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(SpendingExtension.class)
public @interface Spending {
    String category() default "";
    String description();
    double amount();
    CurrencyValues currency() default  CurrencyValues.RUB;
}