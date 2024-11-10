package guru.qa.niffler.helper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

public class EnumHelper {

    public static @Nonnull <T extends Enum<?>> T getRandomEnum(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[new Random().nextInt(enumConstants.length)];
    }

    public static @Nullable <T extends Enum<?>> T getEnumByNameIgnoreCase(Class<T> enumClass, String name) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static @Nullable <T extends Enum<?>> T getEnumByName(Class<T> enumClass, String name) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equals(name)).findAny().orElse(null);
    }

}