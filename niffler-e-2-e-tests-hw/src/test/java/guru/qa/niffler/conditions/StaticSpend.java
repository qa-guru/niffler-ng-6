package guru.qa.niffler.conditions;

import javax.annotation.Nonnull;
import java.util.Comparator;

public record StaticSpend(
        String category,
        String amount,
        String description,
        String date)
        implements Comparable<StaticSpend> {

    @Override
    public int compareTo(@Nonnull StaticSpend spend) {
        return Comparator.comparing(StaticSpend::category)
                .thenComparing(StaticSpend::amount)
                .thenComparing(StaticSpend::description)
                .thenComparing(StaticSpend::date)
                .compare(this, spend);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        StaticSpend that = (StaticSpend) o;
        return date.equals(that.date) &&
                amount.equals(that.amount) &&
                category.equals(that.category) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\"static_spend\" : {" +
                "\"category\":\"" + category + "\"," +
                "\"amount\":\"" + amount + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"date\":\"" + date + "\"" +
                "}";
    }
}