package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.animal().name();
    }

    public static String randomSentence(int wordsCount) {
        String firstWord = faker.gameOfThrones().character();
        String secondWord = faker.relationships().any();
        String thirdWord = faker.harryPotter().character();
        return firstWord + "is" + secondWord + "of" + thirdWord;
    }

    public static String randomPassword() {
        return faker.internet().password(3, 12);
    }
}
