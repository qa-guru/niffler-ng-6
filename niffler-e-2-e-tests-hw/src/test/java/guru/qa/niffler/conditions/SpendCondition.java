package guru.qa.niffler.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.ex.SpendMismatchException;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.CheckResult.accepted;

@ParametersAreNonnullByDefault
public class SpendCondition {

    @Nonnull
    private static WebElementsCondition spends(List<SpendJson> expectedSpends) {
        return new WebElementsCondition() {

            private String expectedSpendsText;

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {

                if (expectedSpends.size() > elements.size()) {
                    throw new SpendMismatchException("List size mismatch (expected: [%s], actual: [%s])"
                            .formatted(expectedSpends.size(), elements.size()));
                }


                // Counts the occurrences of each StaticSpend object created from the expectedSpends list and save in map.
                Map<StaticSpend, Integer> expectedStaticSpendsCount = new HashMap<>();
                expectedSpends.stream()
                        .map(spend ->
                                new StaticSpend(
                                        spend.getCategory().getName(),
                                        getAmountVal(spend.getAmount(), spend.getCurrency()),
                                        spend.getDescription(),
                                        getDateVal(spend.getSpendDate())))
                        .forEach(spend ->
                                expectedStaticSpendsCount.put(
                                        spend,
                                        expectedStaticSpendsCount.getOrDefault(spend, 0) + 1)
                        );

                expectedSpendsText = expectedStaticSpendsCount.toString();

                // Counts the occurrences of each StaticSpend object created from the founded spend list and save in map.
                Map<StaticSpend, Integer> actualStaticSpendsCount = new HashMap<>();
                elements.stream()
                        .map(el -> {
                            List<WebElement> spendCells = el.findElements(By.tagName("td"));
                            return new StaticSpend(
                                    spendCells.get(1).getText(),
                                    spendCells.get(2).getText(),
                                    spendCells.get(3).getText(),
                                    spendCells.get(4).getText()
                            );
                        })
                        .forEach(
                                spend -> actualStaticSpendsCount.put(
                                        spend,
                                        expectedStaticSpendsCount.getOrDefault(spend, 0) + 1));

                // Compare spends and put StaticSpend with diff in new map, if expected count greater then actual
                Map<StaticSpend, Integer> notFoundSpends = new HashMap<>();
                expectedStaticSpendsCount.keySet()
                        .forEach(expectedSpend -> {
                            int expectedCount = expectedStaticSpendsCount.get(expectedSpend);
                            int actualCount = actualStaticSpendsCount.getOrDefault(expectedSpend, 0);
                            if (expectedCount > actualCount) {
                                notFoundSpends.put(expectedSpend, expectedCount - actualCount);
                            }

                        });


                // SECOND VERSION EASY TO UNDERSTAND BUT SLOWER
                /*
                // Convert expected SpendJson to StaticSpend
                List<StaticSpend> expectedStaticSpends = expectedSpends.stream()
                        .map(spend ->
                                new StaticSpend(
                                        spend.getId() != null
                                                ? spend.getId().toString()
                                                : "",
                                        spend.getCategory().getName(),
                                        getAmountVal(spend.getAmount(), spend.getCurrency()),
                                        spend.getDescription(),
                                        getDateVal(spend.getSpendDate())))
                        .collect(Collectors.toList())

                // Convert elements to StaticSpend
                List<StaticSpend> actualStaticSpends = elements.stream()
                        .map(el -> {
                            List<WebElement> spendCells = el.findElements(By.tagName("td"));
                            return new StaticSpend(
                                    "",
                                    spendCells.get(1).getText(),
                                    spendCells.get(2).getText(),
                                    spendCells.get(3).getText(),
                                    spendCells.get(4).getText()
                            );
                        })
                        .collect(Collectors.toList())

                // Sort collections
                Collections.sort(expectedStaticSpends);
                Collections.sort(actualStaticSpends);

                // Search expected in actual StaticSpend. If spend not found add or increase value.
                int lastPos = 0;
                List<StaticSpend> notFoundSpends = new ArrayList<>();
                for (StaticSpend expectedStaticSpend : expectedStaticSpends) {
                    boolean isFound = false;
                    for (int j = lastPos; j < actualStaticSpends.size(); j++) {
                        if (expectedStaticSpend.equals(actualStaticSpends.get(j))) {
                            isFound = true;
                            lastPos = j;
                            break;
                        }

                    }
                    if (!isFound)
                        notFoundSpends.put(
                                expectedStaticSpend,
                                notFoundSpends.getOrDefault(expectedStaticSpend, 0) + 1);
                }*/

                if (!notFoundSpends.isEmpty()) {
                    final String message = "Some of expected spend(s) not found: " + notFoundSpends;
                    throw new SpendMismatchException(message);
                }

                return accepted();
            }


            @Nonnull
            @Override
            public String toString() {
                return expectedSpendsText;
            }

        };
    }

    @Nonnull
    public static WebElementsCondition spends(SpendJson expectedSpend, @Nullable SpendJson... expectedSpends) {
        List<SpendJson> spends = new ArrayList<>();
        spends.add(expectedSpend);
        if (ArrayUtils.isNotEmpty(expectedSpends))
            spends.addAll(Arrays.asList(expectedSpends));
        return spends(spends);
    }

    @Nonnull
    private static WebElementsCondition spendsEquals(List<SpendJson> expectedSpends) {

        return new WebElementsCondition() {

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {

                if (expectedSpends.size() != elements.size())
                    throw new SpendMismatchException("List size mismatch (expected: [%s], actual: [%s])");

                Map<StaticSpend, Integer> notFoundSpends = new HashMap<>();
                for (int i = 0; i < expectedSpends.size(); i++) {

                    List<WebElement> spendCells = elements.get(i).findElements(By.tagName("td"));

                    final StaticSpend expectedSpend = new StaticSpend(
                            expectedSpends.get(i).getCategory().getName(),
                            getAmountVal(expectedSpends.get(i).getAmount(), expectedSpends.get(i).getCurrency()),
                            expectedSpends.get(i).getDescription(),
                            getDateVal(expectedSpends.get(i).getSpendDate()));

                    final StaticSpend actualSpend = new StaticSpend(
                            spendCells.get(1).getText(),
                            spendCells.get(2).getText(),
                            spendCells.get(3).getText(),
                            spendCells.get(4).getText());

                    if (!expectedSpend.equals(actualSpend)) {
                        notFoundSpends.put(
                                expectedSpend,
                                notFoundSpends.getOrDefault(expectedSpend, 0) + 1);
                    }

                }

                if (!notFoundSpends.isEmpty()) {
                    final String message = "Some of expected spend(s) not found: " + notFoundSpends;
                    throw new SpendMismatchException(message);
                }

                return accepted();

            }

            @Nonnull
            @Override
            public String toString() {
                return expectedSpends.toString();
            }

        };

    }

    @Nonnull
    public static WebElementsCondition spendsEquals(SpendJson expectedSpend, @Nullable SpendJson... expectedSpends) {
        List<SpendJson> spends = new ArrayList<>();
        spends.add(expectedSpend);
        if (ArrayUtils.isNotEmpty(expectedSpends))
            spends.addAll(Arrays.asList(expectedSpends));
        return spendsEquals(spends);
    }

    private static String getAmountVal(Double amount, CurrencyValues currency) {
        var amountVal = amount % 10 == 0
                ? amount.intValue()
                : amount;
        return amountVal + " " + currency.getSymbol();
    }

    private static String getDateVal(Date date) {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(date);
    }

}

