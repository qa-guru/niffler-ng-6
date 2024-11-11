package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.rest.SpendJson;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;

public class SpendConditions {

    @Nonnull
    public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends) {
        return new WebElementsCondition() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (expectedSpends.length != elements.size()) {
                    String message = String.format("Table size mismatch (expected: %d, actual: %d)",
                            expectedSpends.length, elements.size());
                    throw new SpendMismatchException(message);
                }

                for (int i = 0; i < elements.size(); i++) {
                    WebElement row = elements.get(i);
                    SpendJson expectedSpend = expectedSpends[i];
                    List<WebElement> cells = row.findElements(By.tagName("td"));

                    // Проверка категории
                    String actualCategory = cells.get(1).getText().trim();
                    if (!actualCategory.equals(expectedSpend.category().name())) {
                        String message = String.format("Spend category mismatch (expected: %s, actual: %s)",
                                expectedSpend.category().name(), actualCategory);
                        throw new SpendMismatchException(message);
                    }

                    // Проверка суммы
                    String actualAmount = cells.get(2).getText().replace("₽", "").trim();
                    String expectedAmount = String.format("%.0f", expectedSpend.amount());
                    if (!actualAmount.equals(expectedAmount)) {
                        String message = String.format("Spend amount mismatch (expected: %s, actual: %s)",
                                expectedAmount, actualAmount);
                        throw new SpendMismatchException(message);
                    }

                    // Проверка описания
                    String actualDescription = cells.get(3).getText().trim();
                    if (!actualDescription.equals(expectedSpend.description())) {
                        String message = String.format("Spend description mismatch (expected: %s, actual: %s)",
                                expectedSpend.description(), actualDescription);
                        throw new SpendMismatchException(message);
                    }

                    // Проверка даты
                    String actualDate = cells.get(4).getText().trim();
                    String expectedDate = dateFormat.format(expectedSpend.spendDate());
                    if (!actualDate.equals(expectedDate)) {
                        String message = String.format("Spend date mismatch (expected: %s, actual: %s)",
                                expectedDate, actualDate);
                        throw new SpendMismatchException(message);
                    }
                }

                return accepted();
            }

            @Override
            public String toString() {
                return List.of(expectedSpends).toString();
            }
        };
    }

    // Custom exception class to handle spend mismatches
    public static class SpendMismatchException extends AssertionError {
        public SpendMismatchException(String message) {
            super(message);
        }
    }
}