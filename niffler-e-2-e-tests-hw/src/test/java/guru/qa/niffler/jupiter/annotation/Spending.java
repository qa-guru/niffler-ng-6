package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Creating new spending:<br/>
 * <b>* date</b>: Default = "". Converting strings with format «MM/dd/yyyy» to Date. Throws exception if value not parseable<br/><br/>
 * <b>* username</b>: Default = "". Use this if <b><u>test does not have</u></b> annotation @CreateNewUser<br/><br/>
 * <b>* category</b>: Default = "". Do not use is test have @Category. To use default value set <b><i>notGenerateCategory</i></b> = true.<br/><br/>
 * <b>* description</b>: Default = "". To use default value set <b><i>notGenerateDescription</i></b> = true.
 * <b>* amount</b>: Default = "". To use default value set <b><i>notGenerateAmount</i></b> = true.
 * <b>* currency</b>: Default = "". To use default value set <b><i>notGenerateCurrency</i></b> = true.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(SpendingExtension.class)
public @interface Spending {

    /**
     * <h7><b>Date pattern "MM/dd/yyyy"</b></h7>
     */
    String date() default "";

    String category() default "";

    String description() default "";

    double amount() default 0;

    CurrencyValues currency() default CurrencyValues.USD;

    boolean notGenerateAmount() default false;

    boolean notGenerateCurrency() default false;

}