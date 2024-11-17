package guru.qa.niffler.ex;

public class ScreenshotException extends RuntimeException {
    public ScreenshotException(String message) {
        super(message);
    }

    public ScreenshotException(String message, Throwable cause) {
        super(message);
    }
}
