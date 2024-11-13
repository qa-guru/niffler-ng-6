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
    private final boolean hasDiff;

    public ScreenDiffResult(BufferedImage expected, BufferedImage actual) {
        this.expected = expected;
        this.actual = actual;
        this.diff = new ImageDiffer().makeDiff(expected, actual);
        hasDiff = diff.hasDiff();
    }

    public ScreenDiffResult(BufferedImage expected, BufferedImage actual, double percent) {
        this.expected = expected;
        this.actual = actual;
        this.diff = new ImageDiffer().makeDiff(expected, actual);
        this.hasDiff = hasDiff(expected,percent);
    }

    private boolean hasDiff(BufferedImage expected, double percent) {
        if (percent < 0.0 || percent > 0.2) {
            throw new IllegalArgumentException("Illegal percent value. Allowed between [0, 0.2]");
        }
        int maxDiffPixels = (int) (expected.getWidth() * expected.getHeight() * percent);
        return diff.getDiffSize() > maxDiffPixels;
    }

    @Override
    public boolean getAsBoolean() {
        if (hasDiff) {
            ScreenshotTestExtension.setExpected(expected);
            ScreenshotTestExtension.setActual(actual);
            ScreenshotTestExtension.setDiff(diff.getMarkedImage());
        }
        return hasDiff;
    }
}