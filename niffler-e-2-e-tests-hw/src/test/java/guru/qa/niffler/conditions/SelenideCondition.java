package guru.qa.niffler.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import lombok.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SelenideCondition {

    public static WebElementCondition child(@NonNull By childSelector, @NonNull WebElementCondition condition) {
        return new WebElementCondition("child " + childSelector + " with " + condition.getName()) {
            @NonNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                WebElement child = element.findElement(childSelector);
                return condition.check(driver, child);
            }
        };
    }

}