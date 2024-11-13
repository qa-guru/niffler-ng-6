package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.rest.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class SpendConditions {

    public static WebElementsCondition spends(SpendJson... expectedSpends) {
        return new WebElementsCondition() {
            final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            private final String spends = Arrays.stream(expectedSpends)
                    .map(sp -> "category name: " + sp.category().name() +
                            ", amount: " + sp.amount() +
                            ", description: " + sp.description() +
                            ", date: " + formatter.format(sp.spendDate()))
                    .toList().toString();

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected spends given");
                }
                if (expectedSpends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedSpends.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                List<String> actualSpends = new ArrayList<>();

                for (int i = 0; i < elements.size(); i++) {
                    List<WebElement> actualSpend = elements.get(i).findElements(By.cssSelector("td"));
                    String category = actualSpend.get(1).getText();
                    String amount = actualSpend.get(2).getText().split(" ")[0];// 1000 Rubl
                    String description = actualSpend.get(3).getText();
                    String date = formattedDate(actualSpend.get(4).getText());

                    actualSpends.add("category name: " + category +
                            ", amount: " + amount +
                            ", description: " + description +
                            ", date: " + date);

                    if (passed) {
                        SpendJson expectedSpend = expectedSpends[i];
                        String expectedDate = formatter.format(expectedSpend.spendDate());

                        passed = expectedSpend.category().name().equals(category) &&
                                expectedSpend.amount().equals(Double.parseDouble(amount)) &&
                                expectedSpend.description().equals(description) &&
                                expectedDate.equals(date);
                    }
                }

                if (!passed) {
                    final String message = String.format(
                            "List spends mismatch (expected: %s, actual: %s)", spends, actualSpends
                    );
                    return rejected(message, actualSpends);
                }
                return accepted();
            }

            private String formattedDate(String dateInString) {
                try {
                    Date parse = formatter.parse(dateInString);
                    return formatter.format(parse);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public String toString() {
                return spends;
            }
        };
    }

}
