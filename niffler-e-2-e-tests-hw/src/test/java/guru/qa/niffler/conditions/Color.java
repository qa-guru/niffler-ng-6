package guru.qa.niffler.conditions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Color {

    YELLOW("rgba(255, 183, 3, 1)"),
    GREEN("rgba(53, 173, 123, 1)"),
    ORANGE("rgba(251, 133, 0, 1)"),
    BLUE100("rgba(41, 65, 204, 1)"),
    AZURE("rgba(33, 158, 188, 1)"),
    BLUE200("rgba(22, 41, 149, 1)"),
    RED("rgba(247, 89, 67, 1)"),
    SKYBLUE("rgba(99, 181, 226, 1)"),
    PURPLE("rgba(148, 85, 198, 1)");

    private final String rgba;

    public static Color getEnumByRgba(String rgba) {
        return Arrays.stream(values())
                .filter(color -> color.getRgba().equalsIgnoreCase(rgba))
                .findFirst()
                .orElse(null);
    }

}
