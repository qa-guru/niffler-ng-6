package utils;

import com.github.javafaker.Faker;

public class FakerUtils {

    static Faker faker = new Faker();
    public static final String USER_NAME = faker.name().firstName();
    public static final String PASSWORD = faker.number().digits(5);
    public static final String SECOND_PASSWORD = faker.number().digits(5);
}
