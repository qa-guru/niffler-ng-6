package guru.qa.niffler.conditions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public record Bubble(
        Color color,
        String text
) {

    @Override
    public String toString() {
        return "\"bubble\" : [" +
                "\"color\":" + color + "," +
                "\"text\":\"" + text + "\"" +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bubble bubble = (Bubble) o;
        return color == bubble.color && Objects.equals(text, bubble.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, text);
    }

}
