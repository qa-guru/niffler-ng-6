package guru.qa.niffler.condition;

import com.codeborne.selenide.*;
import guru.qa.niffler.model.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;


public class SpendCondition {

    record SpendingString(String category, String amount, String description, String date) {
    }

    static SimpleDateFormat formater = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends) {
        List<SpendingString> expectedSpendingStrings = Arrays.stream(expectedSpends).map(x -> new SpendingString(x.category().name(),
                String.format("%.0f", x.amount()),
                x.description(),
                formater.format(x.spendDate()))).toList();
        return new WebElementsCondition() {
            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected colors given");
                }

                if (expectedSpendingStrings.size() > elements.size()) {
                    final String message = String.format("List size expected more than actual (expected: %s, actual: %s)", expectedSpendingStrings.size(), elements.size());
                    return rejected(message, elements);
                }
                boolean passed = true;

                List<SpendingString> actualSpendingStrings = new ArrayList<>();
                for (SpendingString expectedSpendingString : expectedSpendingStrings) {
                    int i = 1;
                    for (WebElement element : elements) {
                        List<WebElement> cells = element.findElements(By.xpath("//tr[" + i + "]/td/span"));
                        if (!cells.get(3).getText().equals(expectedSpendingString.description())) {
                        } else {
                            if (passed) {
                                actualSpendingStrings.add(new SpendingString(
                                        cells.get(1).getText(),
                                        cells.get(2).getText().substring(0, cells.get(2).getText().indexOf(" ")),
                                        cells.get(3).getText(),
                                        cells.get(4).getText()
                                ));
                                passed = cells.get(1).getText().equals(expectedSpendingString.category());
                                passed = cells.get(2).getText().substring(0, cells.get(2).getText().indexOf(" ")).equals(expectedSpendingString.amount());
                                passed = cells.get(4).getText().equals(expectedSpendingString.date());
                            }
                        }
                        i++;
                    }
                }

                if (!passed) {
                    final String message = String.format(
                            "List spending mismatch (expected : %s,  actual: %s)",
                            expectedSpendingStrings.toString(), actualSpendingStrings.toString()
                    );
                    return rejected(message, actualSpendingStrings.toString());
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedSpendingStrings.toString();
            }
        };
    }

}
