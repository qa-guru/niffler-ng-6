package guru.qa.niffler.helper;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class DateHelper {

    public static @Nonnull Optional<Date> parseDateByPattern(String date, String pattern) {
        try {
            return Optional.of(new SimpleDateFormat(pattern).parse(date));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

}