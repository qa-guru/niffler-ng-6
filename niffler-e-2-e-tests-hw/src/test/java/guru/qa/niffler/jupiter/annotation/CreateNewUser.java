package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.rest.CurrencyValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CreateNewUser {

    String username() default "";

    String password() default "";

    CurrencyValues currency() default CurrencyValues.USD;

    boolean notGenerateCurrency() default false;

    String firstName() default "";

    String surname() default "";

    String fullName() default "";

    int incomeInvitations() default 0;

    int outcomeInvitations() default 0;

    int friends() default 0;

    Category[] categories() default {};

    Spending[] spendings() default {};

}