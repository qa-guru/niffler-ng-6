package guru.qa.niffler.page.component;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ScreenshotComponent {

    private static final long TIMEOUT = Long.parseLong(System.getProperty("screenshot.timeout", "3000"));

    public static void validateElement(SelenideElement element, BufferedImage expectedScreenshot)
            throws IOException {

        Selenide.sleep(TIMEOUT);
        BufferedImage actual = ImageIO.read(
                Objects.requireNonNull(
                        element.shouldBe(visible).screenshot()
                ));

        assertFalse(new ScreenDiffResult(
                expectedScreenshot,
                actual

        ));
    }

    public static void validateElement(SelenideElement element, BufferedImage expectedScreenshot, double percent)
            throws IOException {

        Selenide.sleep(TIMEOUT);
        BufferedImage actual = ImageIO.read(
                Objects.requireNonNull(
                        element.shouldBe(visible).screenshot()
                ));

        assertFalse(new ScreenDiffResult(
                expectedScreenshot,
                actual,
                percent

        ));
    }

}
