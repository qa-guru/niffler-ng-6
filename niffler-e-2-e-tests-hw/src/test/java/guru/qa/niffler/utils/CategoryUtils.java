package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.CategoryJson;

public class CategoryUtils {

    private static final Faker FAKE = new Faker();

    public static CategoryJson generate() {
        return CategoryJson.builder()
                .name(FAKE.lorem().characters(3, 10))
                .archived(false).build();
    }

}