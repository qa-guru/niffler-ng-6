package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("category")
        CategoryJson category,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username) {

    public SpendJson category(CategoryJson category) {
        return new SpendJson(
                this.id,
                this.spendDate,
                category,
                this.currency,
                this.amount,
                this.description,
                this.username
        );
    }

    public SpendJson username(String username) {
        return new SpendJson(
                this.id,
                this.spendDate,
                this.category,
                this.currency,
                this.amount,
                this.description,
                username
        );
    }
}
