package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.enums.NonStaticBrowserType;
import guru.qa.niffler.jupiter.extension.NonStaticBrowserTypeExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@ExtendWith(NonStaticBrowserTypeExtension.class)
public @interface NonStaticBrowser {
    NonStaticBrowserType[] value();
}