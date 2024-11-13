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
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

  private static final String PAIR_SEPARATE = " : ";

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

  public static WebElementsCondition statBubbles(Bubble... expectedBubbles) {
    return new WebElementsCondition() {
      private final String expectedValues = Arrays.stream(expectedBubbles)
              .map(c -> c.color().rgb + PAIR_SEPARATE + c.text())
              .toList().toString();

      @NotNull
     @Override
     public CheckResult check(Driver driver, List<WebElement> elements) {
       if (ArrayUtils.isEmpty(expectedBubbles)) {
         throw new IllegalArgumentException("No expected bubbles given");
       }
       if (expectedBubbles.length != elements.size()) {
         final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
         return rejected(message, elements);
       }
       boolean passed = true;
       final List<String> actualValues = new ArrayList<>();
       for (int i = 0; i < elements.size(); i++) {
         final WebElement elementToCheck = elements.get(i);
         final Color colorToCheck = expectedBubbles[i].color();
         final String textToCheck = expectedBubbles[i].text();

         final String rgba = elementToCheck.getCssValue("background-color");
         final String amountText = elementToCheck.getText();

         actualValues.add(rgba + PAIR_SEPARATE + amountText);
         if (passed) {
           passed = colorToCheck.rgb.equals(rgba) && textToCheck.equals(amountText);
         }
       }

       if (passed) {
         return accepted(actualValues);
       }

        String formattedText =
                String.format("List bubbles mismatch (expected: %s, actual: %s)", expectedValues, actualValues);
        return rejected(formattedText, actualValues);
     }

     @Override
     public String toString() {
       return expectedValues;
     }
   };
  }

  public static WebElementsCondition statBubblesInAnyOrder(Bubble... expectedBubbles) {
    return new WebElementsCondition() {
      private final String expectedValues = Arrays.stream(expectedBubbles).map(c -> c.color().rgb + PAIR_SEPARATE + c.text())
              .toList().toString();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected bubbles given");
        }
        if (expectedBubbles.length != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
          return rejected(message, elements);
        }

        final List<String> actualPair = new ArrayList<>();
        final List<String> expectedPair = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement elementToCheck = elements.get(i);
          final Color colorToCheck = expectedBubbles[i].color();
          final String textToCheck = expectedBubbles[i].text();

          final String rgba = elementToCheck.getCssValue("background-color");
          final String text = elementToCheck.getText();

          actualPair.add(rgba + PAIR_SEPARATE + textToCheck.toLowerCase());
          expectedPair.add(colorToCheck.rgb + PAIR_SEPARATE + text.toLowerCase());
        }

        Collections.sort(actualPair);
        Collections.sort(expectedPair);

        if(!actualPair.equals(expectedPair)) {
          String formattedText =
                  String.format("List bubbles mismatch (expected: %s, actual: %s)", expectedValues, actualPair);
          return rejected(formattedText, actualPair);
        }
        return accepted(actualPair);
      }

      @Override
      public String toString() {
        return expectedValues;
      }
    };
  }

  public static WebElementsCondition statBubblesContains(Bubble... expectedBubbles) {
    return new WebElementsCondition() {
      private final String expectedValues = Arrays.stream(expectedBubbles).map(c -> c.color().rgb + PAIR_SEPARATE + c.text())
              .toList().toString();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected bubbles given");
        }
        if (expectedBubbles.length > elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
          return rejected(message, elements);
        }

        final List<String> actualPair = new ArrayList<>();
        final List<String> expectedPair = new ArrayList<>();

          for (Bubble expectedBubble : expectedBubbles) {
              final Color colorToCheck = expectedBubble.color();
              final String textToCheck = expectedBubble.text();
              expectedPair.add(colorToCheck.rgb + PAIR_SEPARATE + textToCheck.toLowerCase());
          }

          for (final WebElement elementToCheck : elements) {
              final String rgba = elementToCheck.getCssValue("background-color");
              final String text = elementToCheck.getText();
              actualPair.add(rgba + PAIR_SEPARATE + text.toLowerCase());
          }

        if(!actualPair.containsAll(expectedPair)) {
          String formattedText =
                  String.format("List bubbles mismatch (expected: %s, actual: %s)", expectedValues, actualPair);
          return rejected (formattedText, actualPair);
        }
        return accepted (actualPair);
      }

      @Override
      public String toString() {
        return expectedValues;
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
}
