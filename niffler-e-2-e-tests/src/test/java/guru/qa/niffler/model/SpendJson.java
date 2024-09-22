package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.enums.CurrencyValues;
import guru.qa.niffler.model.submodel.CategoryJson;

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
}