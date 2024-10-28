package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import guru.qa.niffler.enums.Period;
import guru.qa.niffler.helper.EnumHelper;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static java.time.temporal.ChronoUnit.DAYS;

public class SpendUtils {

    private static final Faker FAKE = new Faker();
    private static final Random RANDOM = new Random();
    private static final LocalDate TODAY = LocalDate.now();
    private static final Long MAX_DAYS_DELTA = DAYS.between(LocalDate.of(1970, 1, 1), LocalDate.now());
    private static final Integer PREVIOUS_MONTH_DAYS;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        PREVIOUS_MONTH_DAYS = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static SpendJson generate() {
        return SpendJson.builder()
                .spendDate(generateDate())
                .category(CategoryUtils.generate())
                .currency(EnumHelper.getRandomEnum(CurrencyValues.class))
                .amount(FAKE.number().randomDouble(2, 1, 100000))
                .description(FAKE.lorem().characters(5, 20))
                .build();
    }

    public static SpendJson generateForUser(@NonNull String username) {
         var spend = generate().setUsername(username);
         spend.setCategory(spend.getCategory().setUsername(username));
         return spend;
    }

    private static Date generateDate() {

        var randomDate = switch (EnumHelper.getRandomEnum(Period.class)) {
            case TODAY -> TODAY;
            case LAST_WEEK -> TODAY.minusDays(RANDOM.nextInt(7));
            case LAST_MONTH -> TODAY.minusMonths(RANDOM.nextInt(PREVIOUS_MONTH_DAYS));
            case ALL_TIME ->
                    TODAY.minusDays(PREVIOUS_MONTH_DAYS + RANDOM.nextLong(MAX_DAYS_DELTA - PREVIOUS_MONTH_DAYS));
        };

        return Date.from(randomDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    }

}