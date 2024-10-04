package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.CategoryJson;

public class CategoryUtils {

    private static Faker fake = new Faker();

    public static CategoryJson generate() {
        return new CategoryJson(null, fake.lorem().word(), null, false);
    }

}
