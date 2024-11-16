package guru.qa.niffler.jupiter.annotation;

import com.codeborne.selenide.junit5.ScreenShooterExtension;
import guru.qa.niffler.jupiter.extantion.ScreenShotTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@ExtendWith(ScreenShotTestExtension.class)
public @interface ScreenShotTest {
    String value();
    boolean rewriteExpected() default false;
}
