package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    private RandomDataUtils() {
        // Private constructor to prevent instantiation
    }

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomFirstName() {
        return faker.name().firstName();
    }

    public static String randomLastName() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.color().name() + " " + faker.animal().name() + " " + faker.number().digit();
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    public static String randomAddress() {
        return faker.address().fullAddress();
    }

    public static String randomPhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }
}