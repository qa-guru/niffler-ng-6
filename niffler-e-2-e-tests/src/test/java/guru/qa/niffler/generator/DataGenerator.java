package guru.qa.niffler.generator;

import com.github.javafaker.Faker;

public class DataGenerator {
    private static final Faker faker = new Faker();

    public static String genRandomPassword(int minimumLength, int maximumLength) {
        return faker.internet().password(minimumLength, maximumLength);
    }

    public static String genRandomUsername() {
        return faker.name().username();
    }

    public static String genRandomCategory() {
        return faker.commerce().department();
    }
}
