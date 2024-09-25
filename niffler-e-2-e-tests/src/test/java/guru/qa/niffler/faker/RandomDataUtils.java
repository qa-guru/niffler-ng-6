package guru.qa.niffler.faker;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength);
    }

    public static String randomCategoryName() {
        return faker.commerce().department() + " and " + faker.country().name();
    }
}
