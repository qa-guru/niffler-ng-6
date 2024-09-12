package guru.qa.niffler.faker;

import com.github.javafaker.Faker;

public class FakeData {

    private static final Faker faker = new Faker();

    public static String generateFakeUserName() {
        return faker.name().username();
    }

    public static String generateFakePassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength);
    }

    public static String generateFakeCategory() {
        return faker.commerce().department() + " and " + faker.country().name();
    }
}
