package guru.qa.niffler.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.ex.BubbleMismatchException;
import guru.qa.niffler.ex.ColorMismatchException;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class StatCondition {

    @Nonnull
    public static WebElementCondition color(Color expectedColor) {
        return new WebElementCondition("color " + expectedColor.getRgba()) {

            @Nonnull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                return new CheckResult(
                        expectedColor.getRgba().equals(rgba),
                        rgba
                );
            }

        };
    }

    @Nonnull
    private static WebElementsCondition colorsEquals(List<Color> expectedColors) {

        int delta = expectedColors.size() - new HashSet<>(expectedColors).size();
        if (delta != 0) {
            final String message = "Stat table could not contains duplicated colors. Duplicated colors count: " + delta;
            throw new ColorMismatchException(message);
        }

        return new WebElementsCondition() {

            private final String expectedRgba = expectedColors.stream()
                    .map(c -> c.getRgba())
                    .toList()
                    .toString();

            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {

                if (expectedColors.size() != elements.size()) {
                    final String message = "List size mismatch (expected: %s, actual: %s)"
                            .formatted(expectedColors.size(), elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaList.add(rgba);
                    if (passed) {
                        passed = expectedColors.get(i).getRgba().equals(rgba);
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

            @Nonnull
            @Override
            public String toString() {
                return expectedRgba;
            }

        };

    }

    @Nonnull
    public static WebElementsCondition colorsEquals(Color expectedColor, @Nullable Color... expectedColors) {
        List<Color> colors = new ArrayList<>();
        colors.add(expectedColor);
        if (ArrayUtils.isNotEmpty(expectedColors))
            colors.addAll(Arrays.asList(expectedColors));
        return colorsEquals(colors);
    }

    @Nonnull
    private static WebElementsCondition statBubblesContains(
            boolean isActualAndExpectedSpendSizesShouldEquals,
            List<Bubble> expectedBubbles) {

        int delta = expectedBubbles.size() - new HashSet<>(expectedBubbles).size();
        if (delta != 0) {
            final String message = "Stat table could not contains duplicated values. Duplicated bubbles count: " + delta;
            throw new BubbleMismatchException(message);
        }

        return new WebElementsCondition() {

            private List<Bubble> actualBubbles;
            private List<Bubble> diffBubbles;

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {

                if (isActualAndExpectedSpendSizesShouldEquals) {
                    if (expectedBubbles.size() != elements.size()) {
                        final String message = "Expected list size should equals actual (expected: %s, actual: %s)"
                                .formatted(expectedBubbles.size(), elements.size());
                        throw new BubbleMismatchException(message);
                    }
                } else {
                    if (expectedBubbles.size() > elements.size()) {
                        final String message = "Expected list size should equals or less then actual (expected: %s, actual: %s)"
                                .formatted(expectedBubbles.size(), elements.size());
                        throw new BubbleMismatchException(message);
                    }
                }

                actualBubbles = elements.stream()
                        .map(element ->
                                new Bubble(
                                        Color.getEnumByRgba(element.getCssValue("background-color")),
                                        element.getText()
                                ))
                        .toList();

                diffBubbles = expectedBubbles.stream()
                        .filter(expectedBubble -> !actualBubbles.contains(expectedBubble))
                        .toList();


                if (!diffBubbles.isEmpty()) {
                    final String message = "List colors mismatch (\nexpected: %s,\nactual: %s,\ndiff: %s)".formatted(expectedBubbles, actualBubbles, diffBubbles);
                    throw new BubbleMismatchException(message);
                }

                return accepted();

            }

            @Nonnull
            @Override
            public String toString() {
                return "expected bubbles: %s, actual bubbles: %s, diff bubbles: %s."
                        .formatted(expectedBubbles, actualBubbles, diffBubbles);
            }

        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesContains(Bubble expectedBubble, @Nullable Bubble... expectedBubbles) {
        List<Bubble> bubbles = new ArrayList<>();
        bubbles.add(expectedBubble);
        if (ArrayUtils.isNotEmpty(expectedBubbles))
            bubbles.addAll(Arrays.asList(expectedBubbles));
        return statBubblesContains(false, bubbles);
    }

    @Nonnull
    public static WebElementsCondition statBubblesInAnyOrder(Bubble expectedBubble, @Nullable Bubble... expectedBubbles) {
        List<Bubble> bubbles = new ArrayList<>();
        bubbles.add(expectedBubble);
        if (ArrayUtils.isNotEmpty(expectedBubbles)) {
            bubbles.addAll(Arrays.asList(expectedBubbles));
        }
        return statBubblesContains(true, bubbles);
    }

    @Nonnull
    private static WebElementsCondition statBubblesEquals(List<Bubble> expectedBubbles) {

        int delta = expectedBubbles.size() - new HashSet<>(expectedBubbles).size();
        if (delta != 0) {
            final String message = "Stat table could not contains duplicated values. Duplicated bubbles count: " + delta;
            throw new BubbleMismatchException(message);
        }

        return new WebElementsCondition() {

            private List<Bubble> actualBubbles;
            private List<Bubble> diffBubbles;

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {

                if (expectedBubbles.size() != elements.size()) {
                    final String message = "List size mismatch (expected: %s, actual: %s)"
                            .formatted(expectedBubbles.size(), elements.size());
                    throw new BubbleMismatchException(message);
                }

                actualBubbles = elements.stream()
                        .map(element ->
                                new Bubble(
                                        Color.getEnumByRgba(element.getCssValue("background-color")),
                                        element.getText()
                                ))
                        .toList();

                diffBubbles = new ArrayList<>();
                for (int i = 0; i < expectedBubbles.size(); i++) {
                    if (!actualBubbles.get(i).equals(expectedBubbles.get(i))) {
                        diffBubbles.add(expectedBubbles.get(i));
                    }
                }

                if (!diffBubbles.isEmpty()) {
                    final String message = "List colors mismatch (expected: %s, actual: %s, diff: %s)"
                            .formatted(expectedBubbles, actualBubbles, diffBubbles);
                    return rejected(message, diffBubbles);
                }

                return accepted();

            }

            @Nonnull
            @Override
            public String toString() {
                return "expected bubbles: %s, actual bubbles: %s, diff bubbles: %s."
                        .formatted(expectedBubbles, actualBubbles, diffBubbles);
            }

        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesEquals(Bubble expectedBubble, @Nullable Bubble... expectedBubbles) {
        List<Bubble> bubbles = new ArrayList<>();
        bubbles.add(expectedBubble);
        if (ArrayUtils.isNotEmpty(expectedBubbles))
            bubbles.addAll(Arrays.asList(expectedBubbles));
        return statBubblesEquals(bubbles);
    }

}
