package guru.qa.niffler.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebElementCondition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.ex.ExpectedImageNotFoundException;
import guru.qa.niffler.ex.ScreenshotException;
import guru.qa.niffler.model.allure.ScreenDiff;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Allure;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static com.codeborne.selenide.CheckResult.accepted;

@ParametersAreNonnullByDefault
public final class ScreenshotCondition {

    private static final String PATH_TO_RESOURCES = "niffler-e-2-e-tests-hw/src/test/resources/";
    private static final ObjectMapper OM = new ObjectMapper();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * @param urlToScreenshot           Path to expected screenshot
     * @param percentOfTolerance        Allowed percent of difference [0; 0.2].
     * @param millis                    Wait before making screenshot
     * @param rewriteExpectedAfterCheck Create and save new expected screenshot
     */
    @Nonnull
    public static WebElementCondition screenshot(
            String urlToScreenshot,
            double percentOfTolerance,
            long millis,
            boolean rewriteExpectedAfterCheck
    ) {

        Selenide.sleep(millis);

        return new WebElementCondition("expected screenshot: [%s]".formatted(urlToScreenshot)) {

            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {

                final BufferedImage expectedScreenshot = getExpectedScreenshot(urlToScreenshot);
                BufferedImage actualScreenshot = takeElementScreenshot(element);
                if (rewriteExpectedAfterCheck)
                    saveNewExpectedScreenshot(actualScreenshot, urlToScreenshot);

                ScreenDiffResult diff = new ScreenDiffResult(
                        expectedScreenshot,
                        actualScreenshot,
                        percentOfTolerance
                );

                if (diff.getAsBoolean()) {

                    addAttachment(
                            ScreenDiff.builder()
                                    .expected("data:image/png;base64," + encoder.encodeToString(imageToBytes(expectedScreenshot)))
                                    .actual("data:image/png;base64," + encoder.encodeToString(imageToBytes(actualScreenshot)))
                                    .diff("data:image/png;base64," + encoder.encodeToString(imageToBytes(diff.getDiff().getMarkedImage())))
                                    .build()
                    );
                    String message = percentOfTolerance > 0
                            ? "Expected and actual screenshots not identical"
                            : "Expected and actual screenshots has difference greater then: " + percentOfTolerance;
                    throw new ScreenshotException(message);
                }

                return accepted();

            }
        };
    }

    /**
     * @param urlToScreenshot    Path to expected screenshot
     * @param percentOfTolerance Allowed percent of difference [0; 0.2].
     * @param millis             Wait before making screenshot
     */
    @Nonnull
    public static WebElementCondition screenshot(
            String urlToScreenshot,
            double percentOfTolerance,
            long millis
    ) {
        return (screenshot(urlToScreenshot, percentOfTolerance, millis, false));
    }

    /**
     * @param urlToScreenshot Path to expected screenshot
     * @param millis          Wait before making screenshot
     * @apiNote * percentOfTolerance = 0.01.
     */
    @Nonnull
    public static WebElementCondition screenshot(
            String urlToScreenshot,
            long millis
    ) {
        return (screenshot(urlToScreenshot, 0.01, millis, false));
    }

    /**
     * @param urlToScreenshot           Path to expected screenshot;
     * @param millis                    Wait before making screenshot;
     * @param rewriteExpectedAfterCheck Create and save new expected screenshot;
     * @apiNote * percentOfTolerance = 0.01.
     */
    @Nonnull
    public static WebElementCondition screenshot(
            String urlToScreenshot,
            long millis,
            boolean rewriteExpectedAfterCheck
    ) {
        return (screenshot(urlToScreenshot, 0.01, millis, rewriteExpectedAfterCheck));
    }

    /**
     * @param urlToScreenshot           Path to expected screenshot
     * @param rewriteExpectedAfterCheck Create and save new expected screenshot
     * @apiNote * percentOfTolerance = 0.01.
     * <br>
     * * millis = 0
     */
    @Nonnull
    public static WebElementCondition screenshot(
            String urlToScreenshot,
            boolean rewriteExpectedAfterCheck
    ) {
        return (screenshot(urlToScreenshot, 0.01, 0, rewriteExpectedAfterCheck));
    }

    /**
     * @param urlToScreenshot Path to expected screenshot
     * @apiNote * percentOfTolerance = 0.01.
     * <br>
     * * millis = 0
     */
    @Nonnull
    public static WebElementCondition screenshot(String urlToScreenshot) {
        return (screenshot(urlToScreenshot, 0.01, 0, false));
    }

    @NotNull
    private static File getFile(String urlToScreenshot) {

        urlToScreenshot = urlToScreenshot.charAt(0) == '/'
                ? urlToScreenshot.substring(0, urlToScreenshot.length() - 1)
                : urlToScreenshot;
        File expectedScreenshotFile = new File(PATH_TO_RESOURCES + urlToScreenshot);
        Path path = expectedScreenshotFile.toPath();

        if (Files.notExists(path))
            throw new ExpectedImageNotFoundException("File not found by path: " + expectedScreenshotFile.getAbsolutePath());

        if (Files.isDirectory(path))
            throw new IllegalStateException(expectedScreenshotFile.getAbsolutePath() + " (Is a directory)");

        return expectedScreenshotFile;

    }

    private static BufferedImage getExpectedScreenshot(String pathToFile) {
        try {
            return ImageIO.read(new ClassPathResource(pathToFile).getInputStream());
        } catch (IOException e) {
            throw new ScreenshotException("Unable to parse expected file from: " + pathToFile, e);
        }
    }

    private static void saveNewExpectedScreenshot(BufferedImage img, String urlToScreenshot) {
        try {
            Files.write(
                    Path.of(PATH_TO_RESOURCES + urlToScreenshot).toAbsolutePath(),
                    imageToBytes(img));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage takeElementScreenshot(WebElement element) {
        try {
            File screenshot = element.getScreenshotAs(OutputType.FILE);
            System.out.println("ПУть: " + screenshot.getAbsolutePath());
            return ImageIO.read(screenshot);
        } catch (IOException e) {
            throw new ScreenshotException("Unable to capture screenshot for element", e);
        }
    }

    private static void addAttachment(ScreenDiff screenDiff) {
        try {
            Allure.addAttachment(
                    "Screenshot diff",
                    "application/vnd.allure.image.diff",
                    OM.writeValueAsString(screenDiff)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
