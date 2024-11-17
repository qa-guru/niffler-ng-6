package guru.qa.niffler.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.ex.BubbleMismatchException;
import guru.qa.niffler.ex.ColorMismatchException;
import io.netty.handler.codec.MessageAggregationException;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

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

}
