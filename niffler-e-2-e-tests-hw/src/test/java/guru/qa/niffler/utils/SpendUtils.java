package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import static java.time.temporal.ChronoUnit.DAYS;

public class SpendUtils {

    private static final Faker fake = new Faker();

    public static SpendJson generate() {
        return new SpendJson(
                null,
                generateDate(),
                CategoryUtils.generate(),
                CurrencyValues.values()[new Random().nextInt(0, CurrencyValues.values().length - 1)],
                fake.number().randomDouble(2, 1, 100000),
                fake.lorem().word(),
                null
        );
    }

    private static Date generateDate() {
        var now = LocalDate.now();
        var daysBetween = DAYS.between(LocalDate.of(1970, 1, 1), now);
        var randomDate = now.minusDays(new Random().nextLong(0L, daysBetween));

        return Date.from(randomDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
