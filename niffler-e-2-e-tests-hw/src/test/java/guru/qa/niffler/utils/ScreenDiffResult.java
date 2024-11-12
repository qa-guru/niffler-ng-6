package guru.qa.niffler.utils;

import guru.qa.niffler.jupiter.extension.ScreenshotTestExtension;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;

public class ScreenDiffResult implements BooleanSupplier {

  private final BufferedImage expected;
  private final BufferedImage actual;
  private final ImageDiff diff;
  private final boolean hasDif;

  public ScreenDiffResult(BufferedImage actual, BufferedImage expected) {
    this.actual = actual;
    this.expected = expected;
    this.diff = new ImageDiffer().makeDiff(expected, actual);
    this.hasDif = diff.hasDiff();
  }

  @Override
  public boolean getAsBoolean() {
    if (hasDif) {
      ScreenshotTestExtension.setExpected(expected);
      ScreenshotTestExtension.setActual(actual);
      ScreenshotTestExtension.setDiff(diff.getMarkedImage());
    }
    return hasDif;
  }
}