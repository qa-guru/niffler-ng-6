package guru.qa.niffler.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;

public class SelenideCondition {

    public static WebElementCondition child(@Nonnull By childSelector, @Nonnull WebElementCondition condition) {
        return new WebElementCondition("child " + childSelector + " with " + condition.getName()) {
            @Nonnull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement child = element.findElement(childSelector);
                return condition.check(driver, child);
            }
        };
    }

}