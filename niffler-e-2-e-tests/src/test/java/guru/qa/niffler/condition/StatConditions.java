package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

    @Nonnull
    public static WebElementCondition color(Color expectedColor) {
        return new WebElementCondition("color " + expectedColor.rgb) {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                return new CheckResult(
                        expectedColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    @Nonnull
    public static WebElementsCondition color(@Nonnull Color... expectedColors) {
        return new WebElementsCondition() {

            private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedColors.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedColors.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedColors[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaList.add(rgba);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba);
                    }
                }

                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String message = String.format(
                            "List colors mismatch (expected: %s, actual: %s)", expectedRgba, actualRgba
                    );
                    return rejected(message, actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedRgba;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubbles(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            private final List<String> expectedRgbaList = Arrays.stream(expectedBubbles).map(x -> x.color().rgb).toList();
            private final List<String> expectedAmountList = Arrays.stream(expectedBubbles).map(x -> x.amount()).toList();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubble given");
                }
                if (expectedBubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                final List<String> actualAmountList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedBubbles[i].color();
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String[] amountActuals = elementToCheck.getText().split(" ");
                    final String amountActual = amountActuals[1];
                    actualRgbaList.add(rgba);
                    actualAmountList.add(amountActual);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba);
                        passed = amountActual.contains(expectedBubbles[i].amount());
                    }
                }

                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String actualAmount = actualAmountList.toString();
                    final String message = String.format(
                            "List bubbles mismatch (expected rgba: %s, expected amount: %s, actual rgba: %s, actual amount: %s)",
                            expectedRgbaList.toString(), expectedAmountList.toString(), actualRgba, actualAmount
                    );
                    return rejected(message, actualRgba + " " + actualAmount);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedRgbaList.toString();
            }
        };

    }

    @Nonnull
    public static WebElementsCondition statBubblesInAnyOrder(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            private final List<String> expectedRgbaList = Arrays.stream(expectedBubbles).map(x -> x.color().rgb).toList();
            private final List<String> expectedAmountList = Arrays.stream(expectedBubbles).map(x -> x.amount()).toList();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubble given");
                }
                if (expectedBubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                final List<String> actualAmountList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String[] amountActuals = elementToCheck.getText().split(" ");
                    final String amountActual = amountActuals[1];
                    actualRgbaList.add(rgba);
                    actualAmountList.add(amountActual);
                    if (passed) {
                        passed = expectedRgbaList.contains(rgba);
                        passed = expectedAmountList.contains(amountActual);
                    }
                }

                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String actualAmount = actualAmountList.toString();
                    final String message = String.format(
                            "List bubbles mismatch (expected rgba: %s, expected amount: %s, actual rgba: %s, actual amount: %s)",
                            expectedRgbaList.toString(), expectedAmountList.toString(), actualRgba, actualAmount
                    );
                    return rejected(message, actualRgba + " " + actualAmount);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedRgbaList.toString();
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesContains(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            private final List<String> expectedRgbaList = Arrays.stream(expectedBubbles).map(x -> x.color().rgb).toList();
            private final List<String> expectedAmountList = Arrays.stream(expectedBubbles).map(x -> x.amount()).toList();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubble given");
                }
                if (expectedBubbles.length > elements.size()) {
                    final String message = String.format("List size expected more than actual (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                List<String> actualRgbaList = elements.stream().map(x -> x.getCssValue("background-color")).toList();
                List<String> actualAmountList = elements.stream().map(x -> x.getText().split(" ")).map(x -> x[1]).toList();

                for (int i = 0; i < expectedBubbles.length; i++) {

                    if (passed) {
                        passed = actualRgbaList.contains(expectedRgbaList.get(i));
                        passed = actualAmountList.contains(expectedAmountList.get(i));
                    }
                }

                if (!passed) {
                    final String message = String.format(
                            "List bubbles mismatch (expected rgba: %s, expected amount: %s, actual rgba: %s, actual amount: %s)",
                            expectedRgbaList.toString(), expectedAmountList.toString(), actualRgbaList.toString(), actualAmountList.toString()
                    );
                    return rejected(message, actualRgbaList.toString() + " " + actualAmountList.toString());
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedRgbaList.toString();
            }
        };
    }

}
