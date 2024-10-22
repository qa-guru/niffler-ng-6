package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.enums.TypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static guru.qa.niffler.enums.TypeEnum.EMPTY;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserType {
  TypeEnum value() default EMPTY;
}