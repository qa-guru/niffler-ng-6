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
            private final String expectedValues = Arrays.stream(expectedBubbles)
                    .map(b -> b.color().rgb + " " + b.text())
                    .toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }
                boolean passed = true;
                final List<String> actualValuesList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Bubble bubbleToCheck = expectedBubbles[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String text = elementToCheck.getText();

                    actualValuesList.add(rgba + " " + text);

                    if (passed) {
                        passed = bubbleToCheck.color().rgb.equals(rgba) && bubbleToCheck.text().equals(text);
                    }
                }
                if (!passed) {
                    final String actualValues = actualValuesList.toString();
                    final String message = String.format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedValues, actualValues
                    );
                    return rejected(message, actualValues);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedValues;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesInAnyOrder(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private final String expectedValues = Arrays.stream(expectedBubbles)
                    .map(b -> b.color().rgb + " " + b.text())
                    .toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                List<String> actualValuesList = new ArrayList<>();
                List<String> expectedValuesList = Arrays.stream(expectedBubbles)
                        .map(b -> b.color().rgb + " " + b.text())
                        .toList();

                for (WebElement element : elements) {
                    final String rgba = element.getCssValue("background-color");
                    final String text = element.getText();
                    actualValuesList.add(rgba + " " + text);
                }

                // Проверяем, совпадают ли списки без учета порядка
                if (actualValuesList.containsAll(expectedValuesList) && expectedValuesList.containsAll(actualValuesList)) {
                    return accepted();
                } else {
                    final String actualValues = actualValuesList.toString();
                    final String message = String.format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedValues, actualValues
                    );
                    return rejected(message, actualValues);
                }
            }

            @Override
            public String toString() {
                return expectedValues;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesContains(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private final String expectedValues = Arrays.stream(expectedBubbles)
                    .map(b -> b.color().rgb + " " + b.text())
                    .toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }

                List<String> actualValuesList = new ArrayList<>();
                List<String> expectedValuesList = Arrays.stream(expectedBubbles)
                        .map(b -> b.color().rgb + " " + b.text())
                        .toList();

                for (WebElement element : elements) {
                    final String rgba = element.getCssValue("background-color");
                    final String text = element.getText();
                    actualValuesList.add(rgba + " " + text);
                }

                // Проверяем, что все expectedValuesList содержатся в actualValuesList
                if (actualValuesList.containsAll(expectedValuesList)) {
                    return accepted();
                } else {
                    final String actualValues = actualValuesList.toString();
                    final String message = String.format(
                            "List bubbles partial mismatch (expected to contain: %s, actual: %s)", expectedValues, actualValues
                    );
                    return rejected(message, actualValues);
                }
            }

            @Override
            public String toString() {
                return expectedValues;
            }
        };
    }
}