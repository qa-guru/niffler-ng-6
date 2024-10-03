package guru.qa.niffler.helper;

public class StringHelper {

    public static <T extends CharSequence> boolean isNullOrEmpty(T text) {
        if (text == null) return true;
        return text.isEmpty();
    }

    public static <T extends CharSequence> boolean isNotNullOrEmpty(T text) {
        return !isNullOrEmpty(text);
    }

    public static <T extends CharSequence> boolean isNullOrBlank(T text) {
        if (text == null) return true;
        return String.valueOf(text).isBlank();
    }

    public static <T extends CharSequence> boolean isNotNullOrBlank(T text) {
        return !isNullOrBlank(text);
    }

}
