package guru.qa.niffler.generator;

import com.github.javafaker.Faker;

public class DataGenerator {
    private static final Faker faker = new Faker();

    public static String genPassword(int minimumLength, int maximumLength) {
        return faker.internet().password(minimumLength, maximumLength);
    }

    public static String genUsername() {
        return faker.name().username();
    }

    public static String genCategory() {
        return faker.commerce().department();
    }
}
